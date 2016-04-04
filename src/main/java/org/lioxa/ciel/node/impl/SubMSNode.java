package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;

/**
 * Sub node (Matrix - Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class SubMSNode extends BinaryNode {

    @Override
    protected void initShape() {
        this.rowSize = this.inputs[0].getRowSize();
        this.colSize = this.inputs[0].getColumnSize();
    }

}
