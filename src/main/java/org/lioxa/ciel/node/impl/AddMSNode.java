package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;

/**
 * Add node (Matrix + Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class AddMSNode extends BinaryNode {

    @Override
    protected void initShape() {
        this.rowSize = this.inputs[0].getRowSize();
        this.colSize = this.inputs[0].getColumnSize();
    }

}
