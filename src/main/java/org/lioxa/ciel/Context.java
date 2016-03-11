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
import org.lioxa.ciel.node.impl.AddNode;
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

    public static final String DEFAULT_OPERATOR_PACKAGE = "org.lioxa.ciel.operator.impl";

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
    public Operator getOperator(Class<? extends Node> target, Class<? extends RealMatrix> matrix) {
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
    public void bindOperators(String pkgName) {
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
    public void removeOperators(String pkgName) {
        this.matchers.remove(pkgName);
    }

    //
    // Graph management.
    //

    WeakHashMap<Class<? extends Node>, WeakHashMap<Node, Object>> graph = new WeakHashMap<>();

    /**
     * Create (or get the existing) a node with the specific type and inputs.
     *
     * @param clazz
     *            The node class which indicates the node type.
     * @param inputs
     *            The input nodes.
     * @return A node. It may be a new created node or an existing node in the
     *         context.
     */
    public Node operate(Class<? extends Node> clazz, Node... inputs) {
        Node node = this.queryGraph(clazz, inputs);
        if (node == null) {
            //
            // TODO: The expression should be further optimized.
            try {
                node = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                String msg = String.format("Failed to create node \"%s\".", clazz.getName());
                throw new RuntimeException(msg, e);
            }
            node.setInputs(inputs);
            this.insertGraph(node);
        }
        return node;
    }

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

    /**
     * Add.
     *
     * @param input0
     *            The input0.
     * @param input1
     *            The input1.
     * @return The term that represent the result.
     */
    public Term add(Term input0, Term input1) {
        Term term;
        if (input0.hasShape(input1)) {
            term = this.operate(AddNode.class, (Node) input0, (Node) input1);
        } else {
            if (input0.isScalar()) {
                term = this.operate(AddNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                term = this.operate(AddNode.class, (Node) input0, (Node) input1);
            } else {
                //
                // MM case, but they do not have the same shape.
                // TODO: Detail information must be given.
                String msg = String.format("Invalid shapes for add.");
                throw new RuntimeException(msg);
            }
        }
        return term;
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
