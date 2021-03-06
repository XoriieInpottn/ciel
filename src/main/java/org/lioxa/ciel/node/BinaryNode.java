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

    //
    // Shape.
    //

    @Override
    public void initShape() {
        this.initShape(this.inputs[0], this.inputs[1]);
    }

    protected abstract void initShape(Node input0, Node input1);

    //
    // Simplify.
    //

    @Override
    public Node simplify() {
        return this.simplify(this.inputs[0], this.inputs[1]);
    }

    protected abstract Node simplify(Node input0, Node input1);

    //
    // Build.
    //

    @Override
    protected void assignOperator(Operator operator) {
        if (!(operator instanceof BinaryOperator)) {
            String optClassName = operator.getClass().getName();
            String msg = String.format("The argument \"operator\": %s is not an binary operator.", optClassName);
            throw new IllegalArgumentException(msg);
        }
        this.operator = operator;
    }

    //
    // Execution.
    //

    @Override
    public RealMatrix execute() {
        if (this.isExpired) {
            //
            // DEBUG
            // System.out.println("Binary node executed.");
            //
            // Get the input matrices.
            Node input0 = this.inputs[0];
            Node input1 = this.inputs[1];
            RealMatrix matrix0 = input0.execute();
            RealMatrix matrix1 = input1.execute();
            //
            // Execute the operator.
            ((BinaryOperator) this.operator).execute(this.matrix, matrix0, matrix1);
            //
            // Set expired to false.
            this.isExpired = false;
        }
        return this.matrix;
    }

}
