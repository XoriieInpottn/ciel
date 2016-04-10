package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;

/**
 * Sub node (Matrix - Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
public class SubMSNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        // TODO Auto-generated method stub
        return null;
    }

}
