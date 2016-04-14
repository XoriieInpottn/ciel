package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.impl.DotOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@DefaultOperator(DotOperator.class)
public class DotNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input1.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0, Node input1) {
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        Context context = this.context;
        Node input0 = this.inputs[0];
        Node input1 = this.inputs[1];
        if (respectTo.equals(input0)) {
            if (respectTo.equals(input1)) {
                Node trans1 = context.newOpt(TransNode.class, input1);
                Node diff0 = context.newOpt(DotNode.class, diff, trans1);
                Node trans0 = context.newOpt(TransNode.class, input0);
                Node diff1 = context.newOpt(DotNode.class, trans0, diff);
                return context.newOpt(AddNode.class, diff0, diff1);
            } else {
                Node trans1 = context.newOpt(TransNode.class, input1);
                return context.newOpt(DotNode.class, diff, trans1);
            }
        } else {
            if (respectTo.equals(input1)) {
                Node trans0 = context.newOpt(TransNode.class, input0);
                return context.newOpt(DotNode.class, trans0, diff);
            } else {
                throw new IllegalStateException();
            }
        }
    }

}
