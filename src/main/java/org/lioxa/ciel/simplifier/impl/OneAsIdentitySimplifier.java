package org.lioxa.ciel.simplifier.impl;

import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.simplifier.Simplifier;
import org.lioxa.ciel.utils.NodeUtils;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class OneAsIdentitySimplifier implements Simplifier {

    @Override
    public Node simplify(InternalNode node) {
        if (!(node instanceof BinaryNode)) {
            String msg = String.format("Node %s is not a BinaryNode.", node.getClass().getName());
            throw new IllegalArgumentException(msg);
        }
        Node input0 = (Node) node.getInput(0);
        Node input1 = (Node) node.getInput(1);
        if (NodeUtils.isAllOne(input0)) {
            return input1;
        }
        if (NodeUtils.isAllOne(input1)) {
            return input0;
        }
        return node;
    }

}