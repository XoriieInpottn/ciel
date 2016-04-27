package org.lioxa.ciel.operator;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link Operator} is the final executor who carry out the operation in the
 * expression node. <br/>
 * In a built expression, every node is assigned an operator. <br/>
 * Another function of operator is to determined the type of the result matrix
 * since it is <b>deeply depends</b> on the organism of the specific operation.
 *
 * @author xi
 * @since Sep 26, 2015
 */
public interface Operator {

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
