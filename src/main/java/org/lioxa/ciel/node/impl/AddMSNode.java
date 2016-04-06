package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;

/**
 * Add node (Matrix + Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class AddMSNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

}
