package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;

/**
 * Sub node (Scalar - Matrix).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class SubSMNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input1.getRowSize();
        this.colSize = input1.getColumnSize();
    }

}
