package org.lioxa.ciel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import org.lioxa.ciel.binding.BindingManager;
import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.gradient.Differentiable;
import org.lioxa.ciel.gradient.Gradient;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.matrix.impl.SingleValueMatrix;
import org.lioxa.ciel.node.ConstNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.LeafNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.OneNode;
import org.lioxa.ciel.node.UpdateNode;
import org.lioxa.ciel.node.VarNode;
import org.lioxa.ciel.node.ZeroNode;
import org.lioxa.ciel.node.impl.AddMSNode;
import org.lioxa.ciel.node.impl.AddNode;
import org.lioxa.ciel.node.impl.AddSMNode;
import org.lioxa.ciel.node.impl.DotNode;
import org.lioxa.ciel.node.impl.MulMSNode;
import org.lioxa.ciel.node.impl.MulNode;
import org.lioxa.ciel.node.impl.MulSMNode;
import org.lioxa.ciel.node.impl.SubMSNode;
import org.lioxa.ciel.node.impl.SubNode;
import org.lioxa.ciel.node.impl.SubSMNode;
import org.lioxa.ciel.node.impl.SumNode;
import org.lioxa.ciel.node.impl.TransNode;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.simplifier.Simplifier;
import org.lioxa.ciel.simplifier.Simplifiers;
import org.lioxa.ciel.simplifier.impl.ConstSimplifier;
import org.lioxa.ciel.utils.Reflects;

/**
 * The {@link Context} is the context of all steps to create, build, execute
 * expressions. <br/>
 * Note that expressions within the same context can be optimized.
 *
 * @author xi
 * @since Sep 25, 2015
 */
public class Context implements Differentiable {

    public static final String DEFAULT_OPERATOR_PACKAGE = "org.lioxa.ciel.operator.impl";

    public Context() {
        this.bindOperators(DEFAULT_OPERATOR_PACKAGE);
        this.simplifiers.add(Simplifiers.get(ConstSimplifier.class));
    }

    //
    // Bindings.
    //

    /**
     * Default Matrix type.
     */
    Class<? extends RealMatrix> defaultMatrixType = RealMatrixImpl.class;

    /**
     * Get default matrix type. <br />
     * The matrix refers to that in the {@link LeafNode} which can also be set
     * manually.
     *
     * @return The default matrix type.
     */
    public Class<? extends RealMatrix> getDefaultMatrixType() {
        return this.defaultMatrixType == null ? RealMatrixImpl.class : this.defaultMatrixType;
    }

    /**
     * Set default matrix type.
     *
     * @param matrixType
     *            The matrix type to be set.
     */
    public void setDefaultMatrixType(Class<? extends RealMatrix> matrixType) {
        this.defaultMatrixType = matrixType;
    }

    /**
     * Binding manager.
     */
    BindingManager bindingManager = new BindingManager();

    /**
     * Get binding manager. <br />
     * The binding manager is used to bind operators to nodes and match
     * appropriate operator for a given node.
     *
     * @return The binding manager.
     */
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
            if (!Reflects.hasInterface(clazz, Operator.class)) {
                String msg = String.format("\"%s\" is not a subclass of Operator.", clazz.getName());
                throw new RuntimeException(msg);
            }
            @SuppressWarnings("unchecked")
            Class<? extends Operator> operatorClass = (Class<? extends Operator>) clazz;
            //
            // At last, the operator instance is added.
            this.bindingManager.addOperatorBinding(binding, operatorClass);
        }
    }

    /**
     * Match a appropriate operator for a given node.
     *
     * @param node
     *            The given node.
     * @return The matches operator.
     */
    public Class<? extends Operator> matchOperator(Node node) {
        Class<? extends Node> target = node.getClass();
        int inputSize = node.getInputSize();
        @SuppressWarnings("unchecked")
        Class<? extends RealMatrix>[] inputMatrixClasses = new Class[inputSize];
        for (int i = 0; i < inputSize; i++) {
            Node input = (Node) node.getInput(i);
            input.build();
            inputMatrixClasses[i] = input.getMatrix().getClass();
        }
        return this.bindingManager.matchOperator(target, inputMatrixClasses);
    }

    //
    // Terms.
    //

    /**
     * Create a new variable term.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return The created term.
     */
    public VarTerm newVar(int rowSize, int colSize) {
        return this.varNode(rowSize, colSize);
    }

    /**
     * Create a new constant term.
     *
     * @param value
     *            The constant value.
     * @return The created term.
     */
    public ConstTerm newConst(RealMatrix value) {
        return this.constNode(value);
    }

    /**
     * Create a new constant term with fixed value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @param value
     *            The value.
     * @return The created term.
     */
    public ConstTerm newConst(int rowSize, int colSize, double value) {
        return this.constNode(rowSize, colSize, value);
    }

    /**
     * Create a new constant term with all "1" value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return The created term.
     */
    public ConstTerm newOne(int rowSize, int colSize) {
        return this.oneNode(rowSize, colSize);
    }

    /**
     * Create a new constant term with all "0" value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return The created term.
     */
    public ConstTerm newZero(int rowSize, int colSize) {
        return this.zeroNode(rowSize, colSize);
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
            term = this.internalNode(AddNode.class, (Node) input0, (Node) input1);
        } else {
            if (input0.isScalar()) {
                //
                // SM case.
                term = this.internalNode(AddSMNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                //
                // MS case.
                term = this.internalNode(AddMSNode.class, (Node) input0, (Node) input1);
            } else {
                //
                // MM case, but they do not have the same shape.
                String shape0 = String.format("(%d, %d)", input0.getRowSize(), input0.getColumnSize());
                String shape1 = String.format("(%d, %d)", input1.getRowSize(), input1.getColumnSize());
                String msg = String.format("Invalid shapes %s and %s for add.", shape0, shape1);
                throw new RuntimeException(msg);
            }
        }
        return term;
    }

    /**
     * Sub.
     *
     * @param input0
     *            The input0.
     * @param input1
     *            The input1.
     * @return The term that represent the result.
     */
    public Term sub(Term input0, Term input1) {
        Term term;
        if (input0.hasShape(input1)) {
            term = this.internalNode(SubNode.class, (Node) input0, (Node) input1);
        } else {
            if (input0.isScalar()) {
                //
                // SM case.
                term = this.internalNode(SubSMNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                //
                // MS case.
                term = this.internalNode(SubMSNode.class, (Node) input0, (Node) input1);
            } else {
                //
                // MM case, but they do not have the same shape.
                String shape0 = String.format("(%d, %d)", input0.getRowSize(), input0.getColumnSize());
                String shape1 = String.format("(%d, %d)", input1.getRowSize(), input1.getColumnSize());
                String msg = String.format("Invalid shapes %s and %s for sub.", shape0, shape1);
                throw new RuntimeException(msg);
            }
        }
        return term;
    }

    /**
     * Multiply.
     *
     * @param input0
     *            The input0.
     * @param input1
     *            The input1.
     * @return The term that represent the result.
     */
    public Term mul(Term input0, Term input1) {
        Term term;
        if (input0.hasShape(input1)) {
            term = this.internalNode(MulNode.class, (Node) input0, (Node) input1);
        } else {
            if (input0.isScalar()) {
                //
                // SM case.
                term = this.internalNode(MulSMNode.class, (Node) input0, (Node) input1);
            } else if (input1.isScalar()) {
                //
                // MS case.
                term = this.internalNode(MulMSNode.class, (Node) input0, (Node) input1);
            } else {
                //
                // MM case, but they do not have the same shape.
                String shape0 = String.format("(%d, %d)", input0.getRowSize(), input0.getColumnSize());
                String shape1 = String.format("(%d, %d)", input1.getRowSize(), input1.getColumnSize());
                String msg = String.format("Invalid shapes %s and %s for mul.", shape0, shape1);
                throw new RuntimeException(msg);
            }
        }
        return term;
    }

    /**
     * Dot.
     *
     * @param input0
     *            The input0.
     * @param input1
     *            The input1.
     * @return The term that represent the result.
     */
    public Term dot(Term input0, Term input1) {
        if (input0.getColumnSize() != input1.getRowSize()) {
            String shape0 = String.format("(%d, %d)", input0.getRowSize(), input0.getColumnSize());
            String shape1 = String.format("(%d, %d)", input1.getRowSize(), input1.getColumnSize());
            String msg = String.format("Invalid shapes %s and %s for dot.", shape0, shape1);
            throw new RuntimeException(msg);
        }
        return this.internalNode(DotNode.class, (Node) input0, (Node) input1);
    }

    /**
     * Transpose.
     *
     * @param input0
     *            The input0.
     * @return The term that represent the result.
     */
    public Term trans(Term input0) {
        return this.internalNode(TransNode.class, (Node) input0);
    }

    /**
     * Sum of the matrix.
     *
     * @param input0
     *            The input0.
     * @return The term that represent the result.
     */
    public Term sum(Term input0) {
        return this.internalNode(SumNode.class, (Node) input0);
    }

    //
    // Graph structure.
    //

    /**
     * Create a new variable node.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return The created node.
     */
    public VarNode varNode(int rowSize, int colSize) {
        VarNode node = new VarNode(rowSize, colSize);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    /**
     * Create a new constant node.
     *
     * @param value
     *            The constant value.
     * @return The created node.
     */
    public ConstNode constNode(RealMatrix value) {
        ConstNode node = new ConstNode(value);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    /**
     * Create a new constant node with fixed value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @param value
     *            The value.
     * @return The created node.
     */
    public ConstNode constNode(int rowSize, int colSize, double value) {
        ConstNode node = new ConstNode(new SingleValueMatrix(rowSize, colSize, value));
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    /**
     * Create a new constant node with all "1" value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column value.
     * @return The created node.
     */
    public OneNode oneNode(int rowSize, int colSize) {
        OneNode node = new OneNode(rowSize, colSize);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    /**
     * Create a new constant node with all "0" value.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return The created node.
     */
    public ZeroNode zeroNode(int rowSize, int colSize) {
        ZeroNode node = new ZeroNode(rowSize, colSize);
        node.setContext(this);
        this.insertGraph(node);
        return node;
    }

    /**
     * Create a (or get the existing) "internal node" with the specific type and
     * inputs.
     *
     * @param nodeType
     *            The node class which indicates the node type.
     * @param inputs
     *            The input nodes.
     * @return A node. It may be a new created node or an existing node in the
     *         context.
     */
    public Node internalNode(Class<? extends InternalNode> nodeType, Node... inputs) {
        InternalNode node = (InternalNode) this.queryGraph(nodeType, inputs);
        if (node != null) {
            return node;
        }
        //
        // If this kind of node has never been created, created it now.
        try {
            node = nodeType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = String.format("Failed to create node %s.", nodeType.getName());
            throw new RuntimeException(msg, e);
        }
        //
        // Then, set the basic info to it.
        node.setContext(this);
        node.setInputs(inputs);
        node.initShape();
        //
        // And insert it into the global graph.
        this.insertGraph(node);
        //
        // At last, the node is simplified, and return.
        return this.simplify(node);
    }

    /**
     * Create a (or get the existing) "update node" with the specific type and
     * inputs.
     *
     * @param nodeType
     *            The node class which indicates the node type.
     * @param target
     *            The target wants to be updated.
     * @param input
     *            The input node.
     * @return A node. It may be a new created node or an existing node in the
     *         context.
     */
    public Node updateNode(Class<? extends UpdateNode> nodeType, Node target, Node input) {
        UpdateNode node = (UpdateNode) this.queryGraph(nodeType, target, input);
        if (node != null) {
            return node;
        }
        //
        // If this kind of node has never been created, created it now.
        try {
            node = nodeType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = String.format("Failed to create node %s.", nodeType.getName());
            throw new RuntimeException(msg, e);
        }
        //
        // Then, set the basic info to it.
        node.setContext(this);
        node.setInputs(target, input);
        //
        // And insert it into the global graph.
        this.insertGraph(node);
        //
        // At last, the node is simplified, and return.
        return this.simplify(node);
    }

    /**
     * The node graph.
     */
    WeakHashMap<Class<?>, WeakHashMap<Node, Object>> graph = new WeakHashMap<>();

    /**
     * Query a specific pattern of node in the graph.
     *
     * @param nodeType
     *            The node class.
     * @param inputs
     *            The input nodes.
     * @return The node that has the specific pattern. If there is no such node,
     *         null will be return.
     */
    Node queryGraph(Class<? extends Node> nodeType, Node... inputs) {
        //
        // Compare node type.
        WeakHashMap<Node, Object> nodes = this.graph.get(nodeType);
        if (nodes == null) {
            return null;
        }
        for (Node node : nodes.keySet()) {
            //
            // Compare inputs pattern.
            if (hasInputs(node, inputs)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Check if the given node has the specific inputs.
     *
     * @param node
     *            The given node.
     * @param inputs
     *            The inputs.
     * @return True if the node's inputs are exactly the same with the given
     *         ones or false if not.
     */
    static boolean hasInputs(Node node, Node... inputs) {
        int inputSize = node.getInputSize();
        if (inputs.length != inputSize) {
            return false;
        }
        for (int i = 0; i < inputSize; i++) {
            if (!node.getInput(i).equals(inputs[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Insert the given node to the graph. <br>
     * Never check if there is already a node which has the same pattern in the
     * graph. It is important to use queryGraph() first to ensure there is no
     * duplicated node.
     *
     * @param node
     *            The node to be inserted into the graph.
     */
    void insertGraph(Node node) {
        Class<?> nodeType = node.getClass();
        WeakHashMap<Node, Object> nodes = this.graph.get(nodeType);
        if (nodes == null) {
            nodes = new WeakHashMap<>();
            this.graph.put(nodeType, nodes);
        }
        nodes.put(node, null);
    }

    /**
     * The simplifiers.
     */
    List<Simplifier> simplifiers = new LinkedList<>();

    Node simplify(Node node) {
        for (Simplifier sim : this.simplifiers) {
            if (node instanceof LeafNode) {
                break;
            }
            //
            // TODO: Update nodes may have problems here.
            node = sim.simplify((InternalNode) node);
        }
        return node.simplify();
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
    // Differentiation.
    //

    /**
     * Gradient.
     */
    Gradient gradient = new Gradient(this);

    @Override
    public Node diff(Node cost, Node respectTo) {
        return this.gradient.diff(cost, respectTo);
    }

    @Override
    public Node[] grad(Node cost, Node... respectTo) {
        return this.gradient.grad(cost, respectTo);
    }

}
