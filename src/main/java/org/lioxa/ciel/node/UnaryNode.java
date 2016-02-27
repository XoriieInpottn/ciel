package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link UnaryNode} is a subclass of {@link Node} which can have only 1
 * inputs. <br/>
 * It is used to carry out binary operations like "exp", "ln", "sigmoid"...
 *
 * @author xi
 * @since Feb 26, 2016
 */
public class UnaryNode extends Node {

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
