package org.lioxa.ciel.operator;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Sep 26, 2015
 */
public abstract class BinaryOperator extends Operator {

    public abstract void execute(RealMatrix result, RealMatrix input0, RealMatrix input1);

    @Override
    public void execute(RealMatrix result, RealMatrix... inputs) {
        this.execute(result, inputs[0], inputs[1]);
    }

}
