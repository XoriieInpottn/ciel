package org.lioxa.ciel.node;

import org.lioxa.ciel.HasOperator;
import org.lioxa.ciel.binding.BindingManager;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.operator.Operator;

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
    public void setInputs(Node[] inputs) {
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
        Class<? extends Node> target = this.getClass();
        @SuppressWarnings("unchecked")
        Class<? extends RealMatrix>[] inputMatrixClasses = new Class[this.inputs.length];
        for (int i = 0; i < this.inputs.length; i++) {
            Node input = this.inputs[i];
            input.build();
            inputMatrixClasses[i] = input.getMatrix().getClass();
        }
        BindingManager bindingManager = this.context.getBindingManager();
        Operator operator = bindingManager.matchOperator(target, inputMatrixClasses);
        if (operator == null) {
            String msg = String.format("Failed to match operator for node %s.", target);
            throw new RuntimeException(msg);
        }
        //
        // Set operator (and matrix).
        this.setOperator(operator);
    }

    protected abstract void setOperator(Operator operator);

}
