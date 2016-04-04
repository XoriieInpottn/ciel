package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;

/**
 * Sub node (Matrix - Matrix or Scalar - Scalar).
 *
 * @author xi
 * @since Mar 11, 2016
 */
public class SubNode extends BinaryNode {

    @Override
    protected void initShape() {
        this.rowSize = this.inputs[0].getRowSize();
        this.colSize = this.inputs[0].getColumnSize();
    }

}
