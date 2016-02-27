package org.lioxa.ciel.operator;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Sep 26, 2015
 */
public abstract class Operator {

    public abstract RealMatrix createMatrix(int rowSize, int colSize);

    public abstract void execute(RealMatrix result, RealMatrix... inputs);

}
