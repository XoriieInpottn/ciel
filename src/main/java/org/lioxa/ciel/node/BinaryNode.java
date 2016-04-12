package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.operator.BinaryOperator;
import org.lioxa.ciel.operator.Operator;

/**
 * The {@link BinaryNode} is a subclass of {@link Node} which can have only 2
 * inputs. <br/>
 * It is used to carry out binary operations like "+", "-", "*", "/"...
 *
 * @author xi
 * @since Feb 26, 2016
 */
public abstract class BinaryNode extends InternalNode {

    @Override
    protected void initShape() {
        this.initShape(this.inputs[0], this.inputs[1]);
    }

    protected abstract void initShape(Node input0, Node input1);

    @Override
    protected void setOperator(Operator operator) {
        if (!(operator instanceof BinaryOperator)) {
            String optClassName = operator.getClass().getName();
            String msg = String.format("The argument \"operator\": %s is not an binary operator.", optClassName);
            throw new IllegalArgumentException(msg);
        }
        this.operator = operator;
        this.matrix = this.operator.createMatrix(this.rowSize, this.colSize);
    }

    @Override
    public Node simplify() {
        return this.simplify(this.inputs[0], this.inputs[1]);
    }

    protected abstract Node simplify(Node input0, Node input1);

    //
    // Executable interface.
    //

    @Override
    public RealMatrix execute() {
        //
        // Get the input matrices.
        Node input0 = this.inputs[0];
        Node input1 = this.inputs[1];
        if (input0.isExpired) {
            input0.execute();
        }
        if (input1.isExpired) {
            input1.execute();
        }
        RealMatrix matrix0 = input0.getMatrix();
        RealMatrix matrix1 = input1.getMatrix();
        //
        // Execute the operator.
        ((BinaryOperator) this.operator).execute(this.matrix, matrix0, matrix1);
        return this.matrix;
    }

}
