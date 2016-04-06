package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.impl.SingleValueMatrix;

/**
 *
 * @author xi
 * @since Apr 6, 2016
 */
public class OneNode extends ConstNode {

    public OneNode(int rowSize, int colSize) {
        super(new SingleValueMatrix(rowSize, colSize, 1));
    }

}
