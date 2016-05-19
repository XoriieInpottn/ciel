package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.SigmoidNode;
import org.lioxa.ciel.operator.UnaryOperator;

/**
 * Sigmoid operator.
 *
 * @author xi
 * @since May 19, 2016
 */
@OperatorBinding(target = SigmoidNode.class, inputs = { RealMatrixImpl.class }, output = RealMatrixImpl.class)
public class SigmoidOperator extends UnaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0) {
        int rowSize = input0.getRowSize();
        int colSize = input0.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double d = input0.get(i, j);
                d = 1 / (1 + Math.exp(-d));
                result.set(i, j, d);
            }
        }
    }

}
