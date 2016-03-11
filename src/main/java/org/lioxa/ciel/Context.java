package org.lioxa.ciel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.lioxa.ciel.binding.Binding;
import org.lioxa.ciel.binding.BindingMatcher;
import org.lioxa.ciel.binding.MatchResult;
import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.utils.Reflects;

/**
 * The {@link Context} is the context of all steps to create, build, execute
 * expressions. <br/>
 * Note that expressions within the same context can be optimized.
 *
 * @author xi
 * @since Sep 25, 2015
 */
public class Context {

    //
    // Bindings.
    //

    Map<String, BindingMatcher> matchers = new HashMap<>();

    /**
     * Get the best operator instance for the given target and matrix.
     *
     * @param target
     *            The target class.
     * @param matrix
     *            The ,matrix class.
     * @return The operator instance.
     */
    Operator getOperator(Class<? extends Node> target, Class<? extends RealMatrix> matrix) {
        MatchResult bestResult = null;
        for (BindingMatcher matcher : this.matchers.values()) {
            MatchResult result = matcher.matchOperator(target, matrix);
            Binding binding = result.getBinding();
            if (binding == null) {
                continue;
            }
            if (bestResult == null) {
                bestResult = result;
            } else {
                if (isBetterThan(result, bestResult)) {
                    bestResult = result;
                }
            }
        }
        return bestResult == null ? null : bestResult.getBinding().getInstance();
    }

    /**
     * Is the given match result better than the current best result?
     *
     * @param result
     *            The given match result.
     * @param bestResult
     *            The current best match result.
     * @return True if "result" better than "bestResult".
     */
    static boolean isBetterThan(MatchResult result, MatchResult bestResult) {
        int bestDist = bestResult.getDistance();
        int dist = result.getDistance();
        if (dist < bestDist) {
            return true;
        } else if (dist == bestDist) {
            if (result.getBinding().getRating() > bestResult.getBinding().getRating()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Bind operators to this context. <br/>
     * When building an expression, the operator is selected automatically from
     * the bindings.
     *
     * @param pkgName
     *            The package name.
     */
    void bindOperators(String pkgName) {
        Collection<Class<?>> classes = Reflects.getClasses(pkgName, false);
        BindingMatcher matcher = new BindingMatcher();
        this.matchers.put(pkgName, matcher);
        for (Class<?> clazz : classes) {
            //
            // First, {@link OperatorBinding} annotation is obtained. Classes
            // without this annotation are ignored.
            OperatorBinding bindingAnn = clazz.getAnnotation(OperatorBinding.class);
            if (bindingAnn == null) {
                continue;
            }
            //
            // Then, there are "target", "matrix" and "rating" to describe the
            // operator.
            Class<? extends Node> target = bindingAnn.target();
            Class<? extends RealMatrix> matrix = bindingAnn.matrix();
            int rating = bindingAnn.rating();
            //
            // Create the operator instance.
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
            //
            // At last, the operator instance is added to matcher.
            matcher.bind(target, matrix, rating, (Operator) instance);
        }
    }

    /**
     * Remove operators bind from the given package.
     *
     * @param pkgName
     *            The package name.
     */
    void removeOperators(String pkgName) {
        this.matchers.remove(pkgName);
    }

    //
    // Graph management.
    //

    WeakHashMap<Class<? extends Node>, WeakHashMap<Node, Object>> graph = new WeakHashMap<>();

    /**
     * Query a specific pattern of node in the graph.
     *
     * @param clazz
     *            The node class.
     * @param inputs
     *            The input nodes.
     * @return The node that has the specific pattern. If there is no such node,
     *         null will be return.
     */
    Node queryGraph(Class<?> clazz, Node... inputs) {
        WeakHashMap<Node, Object> nodes = this.graph.get(clazz);
        if (nodes == null) {
            return null;
        }
        for (Node term : nodes.keySet()) {
            int inputSize = term.getInputSize();
            if (inputs.length != inputSize) {
                continue;
            }
            int i;
            for (i = 0; i < inputSize; i++) {
                if (!term.getInput(i).equals(inputs[i])) {
                    break;
                }
            }
            if (i == inputSize) {
                return term;
            }
        }
        return null;
    }

    /**
     * Insert the given node to the graph.
     *
     * @param node
     *            The node to be inserted into the graph.
     */
    void insertGraph(Node node) {
        Class<? extends Node> clazz = node.getClass();
        WeakHashMap<Node, Object> nodes = this.graph.get(clazz);
        if (nodes == null) {
            nodes = new WeakHashMap<>();
            this.graph.put(clazz, nodes);
        }
        nodes.put(node, null);
    }

    //
    // Build.
    //

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
