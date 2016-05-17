package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.impl.SingleValueMatrix;

/**
 * {@link ZeroNode}. <br />
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class ZeroNode extends ConstNode {

    public ZeroNode(int rowSize, int colSize) {
        super(new SingleValueMatrix(rowSize, colSize, 0));
    }

}
