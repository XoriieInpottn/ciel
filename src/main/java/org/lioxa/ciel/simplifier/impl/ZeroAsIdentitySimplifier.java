package org.lioxa.ciel.simplifier.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.simplifier.Simplifier;
import org.lioxa.ciel.utils.NodeUtils;

/**
 *
 * @author xi
 * @since Apr 9, 2016
 */
public class ZeroAsIdentitySimplifier implements Simplifier {

    @Override
    public Node simplify(InternalNode node) {
        if (!(node instanceof BinaryNode)) {
            String msg = String.format("Node %s is not a BinaryNode.", node.getClass().getName());
            throw new IllegalArgumentException(msg);
        }
        Node input0 = (Node) node.getInput(0);
        Node input1 = (Node) node.getInput(1);
        if (NodeUtils.isAllZero(input0) && (input0.isScalar() || input0.hasShape(input1))) {
            return input1;
        }
        if (NodeUtils.isAllZero(input1) && (input1.isScalar() || input1.hasShape(input0))) {
            return input0;
        }
        return node;
    }

}
