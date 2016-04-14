package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.DotNode;
import org.lioxa.ciel.operator.BinaryOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@OperatorBinding(target = DotNode.class, inputs = { RealMatrixImpl.class, RealMatrixImpl.class })
public class DotOperator extends BinaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0, RealMatrix input1) {
        int rowSize = input0.getRowSize();
        int colSize = input1.getColumnSize();
        int d = input0.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double sum = 0;
                for (int k = 0; k < d; k++) {
                    sum += input0.get(i, k) * input1.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
    }

    @Override
    public RealMatrix createMatrix(int rowSize, int colSize) {
        return new RealMatrixImpl(rowSize, colSize);
    }

}
