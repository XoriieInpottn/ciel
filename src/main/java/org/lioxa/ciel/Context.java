package org.lioxa.ciel;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.OperatorBinding;
import org.lioxa.ciel.operator.Operator;

/**
 * The {@link Context} is the context of all steps to create, build, execute
 * expressions. <br/>
 * Note that expressions within the same context can be optimized.
 *
 * @author xi
 * @since Sep 25, 2015
 */
public class Context {

    /**
     * Bind operators to this context. <br/>
     * When building an expression, the operator is selected automatically from
     * the bindings.
     *
     * @param pkgName
     *            The package name.
     */
    void bindOperators(String pkgName) {
        Collection<Class<?>> classes = getClasses(pkgName, false);
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
            OperatorBinding bindingAnn = clazz.getAnnotation(OperatorBinding.class);
            if (bindingAnn == null) {
                continue;
            }
            Object instance;
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                String msg = String.format("Failed to instance operator \"%s\".", clazz.getName());
                throw new RuntimeException(msg, e);
            }
            if (!(instance instanceof Operator)) {
                String msg = String.format("\"%s\" is not a subclass of Operator.", clazz.getName());
                throw new RuntimeException(msg);
            }
        }
    }

    /**
     * Get classes from the given package.
     *
     * @param pkgName
     *            The package name.
     * @param recursive
     *            If load class from sub packages.
     * @return The classes.
     */
    static Collection<Class<?>> getClasses(String pkgName, boolean recursive) {
        Set<Class<?>> classes = new HashSet<>();
        String pkgPath = pkgName.replace('.', '/');
        Enumeration<URL> urls;
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(pkgPath);
        } catch (IOException e) {
            String msg = String.format("Failed to get resources from \"%s\".", pkgPath);
            throw new RuntimeException(msg, e);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                File dir;
                try {
                    dir = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    continue;
                }
                findByFile(classes, dir, pkgName, recursive);
            } else if ("jar".equals(protocol)) {
                JarFile jar;
                try {
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch (IOException e) {
                    continue;
                }
                findByJar(classes, jar, pkgPath, recursive);
            }
        }
        return classes;
    }

    static void findByFile(Set<Class<?>> classes, File dir, String pkgName, final boolean recursive) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }

        });
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findByFile(classes, file, pkgName + "." + file.getName(), recursive);
            } else {
                String className = file.getName();
                className = className.substring(0, className.length() - ".class".length());
                try {
                    classes.add(Class.forName(pkgName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void findByJar(Set<Class<?>> classes, JarFile jar, String pkgPath, boolean recursive) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entryName.length() == 0) {
                continue;
            }
            if (entryName.charAt(0) == '/') {
                entryName = entryName.substring(1);
            }
            if (!(entryName.startsWith(pkgPath) && entryName.endsWith(".class"))) {
                continue;
            }
            if (!recursive) {
                int len = entryName.length();
                for (int i = pkgPath.length() + 1; i < len; i++) {
                    if (entryName.charAt(i) == '/') {
                        continue;
                    }
                }
            }
            int start = 0;
            if (entryName.charAt(0) == '/') {
                start = 1;
            }
            entryName = entryName.substring(start, entryName.length() - ".class".length());
            String className = entryName.replaceAll("\\/", ".");
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                String msg = String.format("Failed to load class \"%s\".", className);
                throw new RuntimeException(msg, e);
            }
        }
    }

    /**
     * Build the expression given by the {@link Term}. <br/>
     * It mainly:
     * <ul>
     * <li>Assign suitable operators for {@link Node}s</li>
     * <li>Determined and create suitable result matrices for {@link Node}s</li>
     * </ul>
     *
     * @param term
     *            The root {@link Term} of the expression.
     * @return The executable root of the expression.
     */
    public Executable build(Term term) {
        Node node = (Node) term;
        if (node.getOperator() != null) {
            return node;
        }
        int inputSize = term.getInputSize();
        for (int i = 0; i < inputSize; i++) {
            Node input = (Node) term.getInput(i);
            this.build(input);
        }
        return node;
    }

}
