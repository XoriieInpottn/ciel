package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link BinaryNode} is a subclass of {@link Node} which can have only 2
 * inputs. <br/>
 * It is used to carry out binary operations like "+", "-", "*", "/"...
 *
 * @author xi
 * @since Feb 26, 2016
 */
public class BinaryNode extends Node {

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
        this.operator.execute(this.matrix, matrix0, matrix1);
        return this.matrix;
    }

}
