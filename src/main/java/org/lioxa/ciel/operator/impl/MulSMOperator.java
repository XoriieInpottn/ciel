package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.MulSMNode;
import org.lioxa.ciel.operator.BinaryOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@OperatorBinding(target = MulSMNode.class, inputs = { RealMatrixImpl.class, RealMatrixImpl.class })
public class MulSMOperator extends BinaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0, RealMatrix input1) {
        int rowSize = result.getRowSize();
        int colSize = result.getColumnSize();
        double scalar = input0.get(0, 0);
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                result.set(i, j, scalar * input1.get(i, j));
            }
        }
    }

    @Override
    public RealMatrix createMatrix(int rowSize, int colSize) {
        return new RealMatrixImpl(rowSize, colSize);
    }

}
