package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.operator.HasOperator;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.operator.Operators;
import org.lioxa.ciel.operator.UnaryOperator;

/**
 *
 * @author xi
 * @since Apr 27, 2016
 */
public abstract class UpdateNode extends Node implements HasOperator {

    /**
     * Set all input nodes to this node. <br/>
     * At the same time, the output of the given nodes will be set to this node.
     * This method is very important, and it is the first step to initialize a
     * new node.
     *
     * @param target
     *            The target node.
     * @param input
     *            The input node.
     */
    public void setInputs(Node target, Node input) {
        if (this.inputs != null) {
            throw new IllegalStateException("Inputs has been set.");
        }
        this.inputs = new Node[] { target, input };
        target.outputs.put(this, null);
        input.outputs.put(this, null);
        this.rowSize = target.getRowSize();
        this.colSize = target.getColumnSize();
    }

    //
    // HasOperator interface.
    //

    protected Operator operator;

    @Override
    public Operator getOperator() {
        return this.operator;
    }

    //
    // Executable interface.
    //

    @Override
    public RealMatrix execute() {
        //
        // Get the input matrices.
        Node input0 = this.inputs[1];
        RealMatrix matrix0 = input0.execute();
        //
        // Execute the operator.
        ((UnaryOperator) this.operator).execute(this.matrix, matrix0);
        return this.matrix;
    }

    //
    //
    //

    @Override
    public void build() {
        //
        // Get target, input matrix classes, and then match operator.
        Class<? extends Operator> operatorClass = this.context.matchOperator(this);
        if (operatorClass == null) {
            String msg = String.format("Failed to match operator for node %s.", this.getClass());
            throw new RuntimeException(msg);
        }
        //
        // Set operator.
        Operator operator = Operators.get(operatorClass);
        if (!(operator instanceof UnaryOperator)) {
            String optClassName = operator.getClass().getName();
            String msg = String.format("The argument \"operator\": %s is not an unary operator.", optClassName);
            throw new IllegalArgumentException(msg);
        }
        this.operator = operator;
        //
        // Set matrix.
        this.inputs[0].build();
        this.matrix = this.inputs[0].getMatrix();
    }

    @Override
    public Node simplify() {
        return this.simplify(this.inputs[0]);
    }

    protected Node simplify(Node input0) {
        //
        // Simplifications are not encouraged in this kind of node.
        return this;
    }

}
