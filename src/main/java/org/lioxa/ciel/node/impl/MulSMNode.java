package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.NodeUtils;
import org.lioxa.ciel.operator.impl.MulSMOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@DefaultOperator(MulSMOperator.class)
public class MulSMNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input1.getRowSize();
        this.colSize = input1.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0, Node input1) {
        if (NodeUtils.isAllOne(input0)) {
            return input1;
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
                r = context.internalNode(MulNode.class, diff, input1);
            }
        } else {
            if (respectTo.equals(input1)) {
                r = context.internalNode(MulSMNode.class, input0, diff);
            } else {
                throw new IllegalStateException();
            }
        }
        return r;
    }

}
