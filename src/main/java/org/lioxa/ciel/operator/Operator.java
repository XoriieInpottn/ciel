package org.lioxa.ciel.operator;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link Operator} is the final executor who carry out the operation in the
 * expression node. <br/>
 * In a built expression, every node is assigned an operator. <br/>
 * Another function of operator is to create the result matrix since the type of
 * result matrix if <b>deeply depends</b> on the organism of the specific
 * operation.
 *
 * @author xi
 * @since Sep 26, 2015
 */
public abstract class Operator {

    /**
     * Create the result matrix with the given shape. <br/>
     * The shape is usually calculated by the expression node.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            THe column size.
     * @return The created result matrix.
     */
    public abstract RealMatrix createMatrix(int rowSize, int colSize);

    /**
     * Execute the operation.
     *
     * @param result
     *            The reault matrix.
     * @param inputs
     *            The input matrices.
     */
    public abstract void execute(RealMatrix result, RealMatrix... inputs);

}
