package org.lioxa.ciel;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.WeakHashMap;

import org.lioxa.ciel.binding.BindingManager;
import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.LeafNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.VarNode;
import org.lioxa.ciel.node.impl.AddMSNode;
import org.lioxa.ciel.node.impl.AddNode;
import org.lioxa.ciel.node.impl.AddSMNode;
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

    public Context() {
        this.bindOperators(DEFAULT_OPERATOR_PACKAGE);
    }

    //
    // Bindings.
    //

    Class<? extends RealMatrix> defaultMatrixClass = RealMatrixImpl.class;

    public Class<? extends RealMatrix> getDefaultMatrixClass() {
        return this.defaultMatrixClass;
    }

    public void setDefaultMatrixClass(Class<? extends RealMatrix> defaultMatrixClass) {
        this.defaultMatrixClass = defaultMatrixClass;
    }

    BindingManager bindingManager = new BindingManager();

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
        for (Class<?> clazz : classes) {
            //
            // First, OperatorBinding annotation is obtained. Classes
            // without this annotation are ignored.
            OperatorBinding binding = clazz.getAnnotation(OperatorBinding.class);
            if (binding == null) {
                continue;
            }
            //
            // Then, create the operator instance.
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
            // At last, the operator instance is added.
            this.bindingManager.addOperatorBinding(binding, (Operator) instance);
        }
    }

    //
    // Graph management.
    //

    WeakHashMap<Class<? extends Node>, WeakHashMap<Node, Object>> graph = new WeakHashMap<>();

    public Node var(int rowSize, int colSize) {
        Node node = new VarNode(rowSize, colSize);
        this.insertGraph(node);
        return node;
    }

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
                //
                // SM case.
                term = this.operate(AddSMNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                //
                // MS case.
                term = this.operate(AddMSNode.class, (Node) input0, (Node) input1);
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
        if (node.getMatrix() != null) {
            return node;
        }
        //
        // Build.
        if (node instanceof HasOperator) {
            int inputSize = term.getInputSize();
            @SuppressWarnings("unchecked")
            Class<? extends RealMatrix>[] inputMatrixClasses = new Class[inputSize];
            for (int i = 0; i < inputSize; i++) {
                Node input = (Node) term.getInput(i);
                this.build(input);
                inputMatrixClasses[i] = input.getMatrix().getClass();
            }
            Operator operator = this.bindingManager.matchOperator(node.getClass(), inputMatrixClasses);
            ((HasOperator) node).setOperator(operator);
        } else if (node instanceof LeafNode && node.getMatrix() == null) {
            RealMatrix matrix;
            try {
                Constructor<? extends RealMatrix> c;
                c = this.defaultMatrixClass.getConstructor(int.class, int.class);
                matrix = c.newInstance(node.getRowSize(), node.getColumnSize());
            } catch (Exception e) {
                throw new RuntimeException("", e);
            }
            node.setMatrix(matrix);
        }
        return node;
    }

}
