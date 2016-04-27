package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.AddMSNode;
import org.lioxa.ciel.operator.BinaryOperator;

/**
 * Add operator (Matrix + Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
@OperatorBinding(target = AddMSNode.class, inputs = { RealMatrixImpl.class,
        RealMatrixImpl.class }, output = RealMatrixImpl.class)
public class AddMSOperator extends BinaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0, RealMatrix input1) {
        int rowSize = result.getRowSize();
        int colSize = result.getColumnSize();
        double scalar = input1.get(0, 0);
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double value = input0.get(i, j) + scalar;
                result.set(i, j, value);
            }
        }
    }

}
