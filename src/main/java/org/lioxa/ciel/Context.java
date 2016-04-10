package org.lioxa.ciel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.lioxa.ciel.binding.BindingManager;
import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.matrix.impl.SingleValueMatrix;
import org.lioxa.ciel.node.ConstNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.OneNode;
import org.lioxa.ciel.node.VarNode;
import org.lioxa.ciel.node.ZeroNode;
import org.lioxa.ciel.node.impl.AddMSNode;
import org.lioxa.ciel.node.impl.AddNode;
import org.lioxa.ciel.node.impl.AddSMNode;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.simplifier.Simplifier;
import org.lioxa.ciel.simplifier.Simplifiers;
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
        return this.defaultMatrixClass == null ? RealMatrixImpl.class : this.defaultMatrixClass;
    }

    public void setDefaultMatrixClass(Class<? extends RealMatrix> defaultMatrixClass) {
        this.defaultMatrixClass = defaultMatrixClass;
    }

    BindingManager bindingManager = new BindingManager();

    public BindingManager getBindingManager() {
        return this.bindingManager;
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

    public Node newVar(int rowSize, int colSize) {
        Node node = new VarNode(rowSize, colSize);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    public Node newConst(RealMatrix value) {
        Node node = new ConstNode(value);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    public Node newConst(int rowSize, int colSize, double value) {
        Node node = new ConstNode(new SingleValueMatrix(rowSize, colSize, value));
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    public Node newOne(int rowSize, int colSize) {
        Node node = new OneNode(rowSize, colSize);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    public Node newZero(int rowSize, int colSize) {
        Node node = new ZeroNode(rowSize, colSize);
        node.setContext(this);
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
    public Node newOpt(Class<? extends InternalNode> clazz, Node... inputs) {
        InternalNode node = (InternalNode) this.queryGraph(clazz, inputs);
        if (node != null) {
            return node;
        }
        //
        // If this kind of node has never been created, created it now.
        try {
            node = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = String.format("Failed to create node %s.", clazz.getName());
            throw new RuntimeException(msg, e);
        }
        //
        // Then, set the basic info to it.
        node.setContext(this);
        node.setInputs(inputs);
        //
        // And insert it into the global graph.
        this.insertGraph(node);
        //
        // At last, the node is simplified, and return.
        return this.simplify(node);
    }

    Map<Class<?>, Simplifier> simplifiers = new HashMap<>();

    Node simplify(InternalNode node) {
        Simplifiers simsAnn = node.getClass().getAnnotation(Simplifiers.class);
        if (simsAnn == null) {
            return node;
        }
        Class<? extends Simplifier>[] sims = simsAnn.value();
        if (sims == null || sims.length == 0) {
            return node;
        }
        //
        // Start simplify.
        Node node1 = node;
        for (Class<? extends Simplifier> simClass : sims) {
            if (!(node1 instanceof InternalNode)) {
                //
                // Cannot simplify any non-internal nodes.
                break;
            }
            //
            // Get a singleton simplifier instance.
            Simplifier sim = this.simplifiers.get(simClass);
            if (sim == null) {
                try {
                    sim = simClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    String msg = String.format("Failed to create simplifier %s.", simClass.getName());
                    throw new RuntimeException(msg, e);
                }
            }
            //
            // Do simplification.
            node1 = sim.simplify((InternalNode) node1);
        }
        return node1;
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
            term = this.newOpt(AddNode.class, (Node) input0, (Node) input1);
        } else {
            if (input0.isScalar()) {
                //
                // SM case.
                term = this.newOpt(AddSMNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                //
                // MS case.
                term = this.newOpt(AddMSNode.class, (Node) input0, (Node) input1);
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
        node.build();
        return node;
    }

    //
    // Gradient.
    //

    public Node grad(Node cost, Node respectTo) {
        if (cost.getContext() != this || respectTo.getContext() != this) {
            throw new RuntimeException("Any of the nodes may not belong to this context.");
        }
        Set<Node> availables = new HashSet<>();
        List<Node> queue = new LinkedList<>();
        queue.add(cost);
        while (!queue.isEmpty()) {
            Node node = queue.remove(0);
            availables.add(node);
            int inputSize = node.getInputSize();
            for (int i = 0; i < inputSize; i++) {
                queue.add((Node) node.getInput(i));
            }
        }
        if (!availables.contains(respectTo)) {
            String msg = String.format("%s is not a component of %.", respectTo, cost);
            throw new RuntimeException(msg);
        }
        return this.grad(cost, respectTo, availables);
    }

    Node grad(Node cost, Node respectTo, Set<Node> availables) {
        if (respectTo.equals(cost)) {
            return this.newOne(1, 1);
        }
        List<Node> parts = new LinkedList<>();
        for (Node output : respectTo.getOutputs()) {
            if (!availables.contains(output)) {
                continue;
            }
            Node grad = this.grad(cost, output, availables);
            grad = output.diff(grad, respectTo);
            parts.add(grad);
        }
        switch (parts.size()) {
        case 0:
            String msg = String.format("%s is not a component of %.", respectTo, cost);
            throw new RuntimeException(msg);
        case 1:
            return parts.get(0);
        default:
            Iterator<Node> iter = parts.iterator();
            iter.hasNext();
            Node grad = iter.next();
            while (iter.hasNext()) {
                Node grad1 = iter.next();
                grad = this.newOpt(AddNode.class, grad, grad1);
            }
            return grad;
        }
    }
}
