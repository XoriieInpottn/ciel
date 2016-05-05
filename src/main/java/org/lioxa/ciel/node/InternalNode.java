package org.lioxa.ciel.node;

import org.lioxa.ciel.HasOperator;
import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.operator.Operators;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public abstract class InternalNode extends Node implements HasOperator {

    /**
     * Set all input nodes to this node. <br/>
     * At the same time, the output of the given nodes will be set to this node.
     * This method is very important, and it is the first step to initialize a
     * new node.
     *
     * @param inputs
     *            The input nodes.
     */
    public void setInputs(Node... inputs) {
        if (this.inputs != null) {
            throw new IllegalStateException("Inputs has been set.");
        }
        this.inputs = inputs.clone();
        for (Node input : this.inputs) {
            input.outputs.put(this, null);
        }
        this.initShape();
    }

    /**
     * Initialize shape for the node.
     */
    protected abstract void initShape();

    protected Operator operator;

    @Override
    public Operator getOperator() {
        return this.operator;
    }

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
        this.setOperator(operator);
        //
        // Set matrix.
        this.matrix = MatrixUtils.createByOperator(operatorClass, this.rowSize, this.colSize);
    }

    protected abstract void setOperator(Operator operator);

    //
    // Differentiation.
    //

    public abstract Node diff(Node diff, Node respectTo);

}
