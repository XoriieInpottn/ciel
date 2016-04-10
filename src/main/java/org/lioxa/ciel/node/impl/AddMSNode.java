package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.simplifier.Simplifiers;
import org.lioxa.ciel.simplifier.impl.ZeroAsIdentitySimplifier;

/**
 * Add node (Matrix + Scalar).
 *
 * @author xi
 * @since Mar 12, 2016
 */
@Simplifiers({ ZeroAsIdentitySimplifier.class })
public class AddMSNode extends BinaryNode {

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
