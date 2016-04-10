package org.lioxa.ciel.simplifier.impl;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.ConstNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;
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
            Node input = (Node) node.getInput(i);
            inputMatrices[i] = input.getMatrix();
        }
        //
        // execute operation
        Operator operation = node.getOperator();
        RealMatrix resultMatrix = operation.createMatrix(node.getRowSize(), node.getColumnSize());
        operation.execute(resultMatrix, inputMatrices);
        //
        // make result term
        return node.getContext().newConst(resultMatrix);
    }

}
