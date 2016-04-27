package org.lioxa.ciel.operator.impl;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.impl.AddNode;
import org.lioxa.ciel.operator.BinaryOperator;

/**
 * Add operator (matrix - matrix or scalar - scalar).
 *
 * @author xi
 * @since Mar 11, 2016
 */
@OperatorBinding(target = AddNode.class, inputs = { RealMatrixImpl.class,
        RealMatrixImpl.class }, output = RealMatrixImpl.class)
public class AddOperator extends BinaryOperator {

    @Override
    public void execute(RealMatrix result, RealMatrix input0, RealMatrix input1) {
        int rowSize = result.getRowSize();
        int colSize = result.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double value = input0.get(i, j) + input1.get(i, j);
                result.set(i, j, value);
            }
        }
    }

}
