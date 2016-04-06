package org.lioxa.ciel.node;

import org.lioxa.ciel.HasOperator;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.operator.UnaryOperator;

/**
 * The {@link UnaryNode} is a subclass of {@link Node} which can have only 1
 * inputs. <br/>
 * It is used to carry out binary operations like "exp", "ln", "sigmoid"...
 *
 * @author xi
 * @since Feb 26, 2016
 */
public abstract class UnaryNode extends Node implements HasOperator {

    @Override
    protected void initShape() {
        this.initShape(this.inputs[0]);
    }

    protected abstract void initShape(Node input0);

    //
    // HasOperator interface.
    //

    protected UnaryOperator operator;

    @Override
    public Operator getOperator() {
        return this.operator;
    }

    @Override
    public void setOperator(Operator operator) {
        if (this.operator != null) {
            throw new IllegalStateException("Operator has been set.");
        }
        if (!(operator instanceof UnaryOperator)) {
            String optClassName = operator.getClass().getName();
            String msg = String.format("The argument \"operator\": %s is not an unary operator.", optClassName);
            throw new IllegalArgumentException(msg);
        }
        this.operator = (UnaryOperator) operator;
        this.matrix = this.operator.createMatrix(this.rowSize, this.colSize);
    }

    //
    // Executable interface.
    //

    @Override
    public RealMatrix execute() {
        //
        // Get the input matrices.
        Node input0 = this.inputs[0];
        if (input0.isExpired) {
            input0.execute();
        }
        RealMatrix matrix0 = input0.getMatrix();
        //
        // Execute the operator.
        this.operator.execute(this.matrix, matrix0);
        return this.matrix;
    }

}
