package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.SumNode;
import org.lioxa.ciel.operator.UnaryOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@OperatorBinding(target = SumNode.class, inputs = { RealMatrixImpl.class })
public class SumOperator extends UnaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0) {
        double sum = 0;
        int rowSize = input0.getRowSize();
        int colSize = input0.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                sum += input0.get(i, j);
            }
        }
        result.set(0, 0, sum);
    }

    @Override
    public RealMatrix createMatrix(int rowSize, int colSize) {
        return new RealMatrixImpl(rowSize, colSize);
    }

}
