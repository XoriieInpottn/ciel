package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;

/**
 * Add node (Matrix + Matrix or Scalar + Scalar).
 *
 * @author xi
 * @since Mar 11, 2016
 */
public class AddNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

}
