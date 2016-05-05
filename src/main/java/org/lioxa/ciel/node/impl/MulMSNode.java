package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.NodeUtils;
import org.lioxa.ciel.operator.impl.MulMSOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@DefaultOperator(MulMSOperator.class)
public class MulMSNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0, Node input1) {
        if (NodeUtils.isAllOne(input1)) {
            return input0;
        }
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        Context context = this.context;
        Node input0 = this.inputs[0];
        Node input1 = this.inputs[1];
        Node r;
        if (respectTo.equals(input0)) {
            if (respectTo.equals(input1)) {
                //
                // Impossible. Must be BUG!
                throw new IllegalStateException();
            } else {
                r = context.internalNode(MulMSNode.class, diff, input1);
            }
        } else {
            if (respectTo.equals(input1)) {
                r = context.internalNode(MulNode.class, input0, diff);
            } else {
                throw new IllegalStateException();
            }
        }
        return r;
    }

}
