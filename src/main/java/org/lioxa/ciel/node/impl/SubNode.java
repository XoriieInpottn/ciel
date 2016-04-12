package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.impl.SubOperator;
import org.lioxa.ciel.utils.NodeUtils;

/**
 * Sub node (Matrix - Matrix or Scalar - Scalar).
 *
 * @author xi
 * @since Mar 11, 2016
 */
@DefaultOperator(SubOperator.class)
public class SubNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input0.getRowSize();
        this.colSize = input0.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0, Node input1) {
        if (NodeUtils.isAllZero(input1)) {
            return input0;
        }
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        // TODO Auto-generated method stub
        return null;
    }

}
