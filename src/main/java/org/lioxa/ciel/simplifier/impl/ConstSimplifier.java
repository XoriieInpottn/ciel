package org.lioxa.ciel.simplifier.impl;

import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.ConstNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.operator.Operators;
import org.lioxa.ciel.simplifier.Simplifier;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class ConstSimplifier implements Simplifier {

    @Override
    public Node simplify(InternalNode node) {
        //
        // check whether all inputs are Constant
        int inputSize = node.getInputSize();
        for (int i = 0; i < inputSize; i++) {
            Node input = (Node) node.getInput(i);
            if (!(input instanceof ConstNode)) {
                return node;
            }
        }
        //
        // get input values
        RealMatrix[] inputMatrices = new RealMatrix[inputSize];
        for (int i = 0; i < inputSize; i++) {
            ConstNode input = (ConstNode) node.getInput(i);
            inputMatrices[i] = input.getValue();
        }
        //
        // Get operator.
        DefaultOperator ann = node.getClass().getAnnotation(DefaultOperator.class);
        Class<? extends Operator> operatorClass;
        if (ann == null || (operatorClass = ann.value()) == null) {
            return node;
        }
        Operator operator = Operators.get(operatorClass);
        //
        // Perform operation.
        RealMatrix resultMatrix = MatrixUtils.createByOperator(operatorClass, node.getRowSize(), node.getColumnSize());
        operator.execute(resultMatrix, inputMatrices);
        //
        // Make result term.
        return node.getContext().constNode(resultMatrix);
    }

}
