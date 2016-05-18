package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.operator.HasOperator;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.operator.Operators;

/**
 * {@link InternalNode}. <br />
 *
 * @author xi
 * @since Apr 7, 2016
 */
public abstract class InternalNode extends Node implements HasOperator {

    //
    // Shape.
    //

    /**
     * Initialize shape for the node.
     */
    public abstract void initShape();

    //
    // Graph structure.
    //

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
    }

    //
    // Operation.
    //

    protected Operator operator;

    @Override
    public Operator getOperator() {
        return this.operator;
    }

    @Override
    public void setOperator(Class<? extends Operator> operatorType) {
        this.setOperator(Operators.get(operatorType));
    }

    @Override
    public void setOperator(Operator operator) {
        if (this.isBuild()) {
            throw new IllegalStateException("Cannot set operator to a node which has already been build.");
        }
        this.operator = operator;
    }

    //
    // Build.
    //

    @Override
    public void build() {
        if (this.isBuild()) {
            return;
        }
        if (this.operator == null) {
            //
            // Get operator class.
            Class<? extends Operator> operatorClass = this.context.matchOperator(this);
            if (operatorClass == null) {
                String msg = String.format("Failed to match operator for node %s.", this.getClass());
                throw new RuntimeException(msg);
            }
            //
            // Set operator.
            Operator operator = Operators.get(operatorClass);
            this.assignOperator(operator);
        }
        //
        // Set matrix.
        this.matrix = MatrixUtils.createByOperator(this.operator.getClass(), this.rowSize, this.colSize);
    }

    protected abstract void assignOperator(Operator operator);

    //
    // Differentiation.
    //

    public abstract Node diff(Node diff, Node respectTo);

}
