package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.UnaryNode;
import org.lioxa.ciel.operator.impl.SigmoidOperator;

/**
 * Sigmoid node. (s(x) = 1 / (1 + exp(-x)))
 *
 * @author xi
 * @since May 19, 2016
 */
@DefaultOperator(SigmoidOperator.class)
public class SigmoidNode extends UnaryNode {

    @Override
    protected void initShape(Node input0) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0) {
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        Context context = this.context;
        Node input0 = this.inputs[0];
        if (!respectTo.equals(input0)) {
            throw new IllegalStateException();
        }
        //
        // s' = s * (1 - s)
        Node result;
        result = context.internalNode(SubSMNode.class, context.oneNode(1, 1), this);
        result = context.internalNode(MulNode.class, this, result);
        return result;
    }

}
