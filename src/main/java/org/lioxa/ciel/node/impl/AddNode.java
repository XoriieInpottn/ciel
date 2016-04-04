package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;

/**
 * Add node (Matrix + Matrix or Scalar + Scalar).
 *
 * @author xi
 * @since Mar 11, 2016
 */
public class AddNode extends BinaryNode {

    @Override
    protected void initShape() {
        this.rowSize = this.inputs[0].getRowSize();
        this.colSize = this.inputs[0].getColumnSize();
    }

}
