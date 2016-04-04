package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class ConstNode extends LeafNode {

    RealMatrix value;

    public ConstNode(RealMatrix value) {
        this.value = value;
        this.rowSize = value.getRowSize();
        this.colSize = value.getColumnSize();
    }

}
