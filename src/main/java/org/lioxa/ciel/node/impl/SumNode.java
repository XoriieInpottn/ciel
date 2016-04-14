package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.UnaryNode;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
public class SumNode extends UnaryNode {

    @Override
    protected void initShape(Node input0) {
        this.rowSize = 1;
        this.colSize = 1;
    }

    @Override
    protected Node simplify(Node input0) {
        if (input0.isScalar()) {
            return input0;
        }
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        Context context = this.context;
        Node input0 = this.inputs[0];
        if (!respectTo.equals(input0)) {
            throw new IllegalStateException();
        }
        Node one = context.newOne(input0.getRowSize(), input0.getColumnSize());
        return context.newOpt(MulSMNode.class, diff, one);
    }

}
