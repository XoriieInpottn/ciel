package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;

/**
 * Sub node (Scalar - Matrix).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class SubSMNode extends BinaryNode {

    @Override
    protected void initShape() {
        this.rowSize = this.inputs[1].getRowSize();
        this.colSize = this.inputs[1].getColumnSize();
    }

}
