package org.lioxa.ciel.node.impl;

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
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
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
        return null;
    }

}
