package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.TransNode;
import org.lioxa.ciel.operator.UnaryOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@OperatorBinding(target = TransNode.class, inputs = { RealMatrixImpl.class })
public class TransOperator extends UnaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input) {
        int rowSize = input.getRowSize();
        int colSize = input.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                result.set(j, i, input.get(i, j));
            }
        }
    }

    @Override
    public RealMatrix createMatrix(int rowSize, int colSize) {
        return new RealMatrixImpl(rowSize, colSize);
    }

}
