package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.BinaryNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.impl.SubSMOperator;

/**
 * Sub node (Scalar - Matrix).
 *
 * @author xi
 * @since Mar 12, 2016
 */
@DefaultOperator(SubSMOperator.class)
public class SubSMNode extends BinaryNode {

    @Override
    protected void initShape(Node input0, Node input1) {
        this.rowSize = input1.getRowSize();
        this.colSize = input1.getColumnSize();
    }

    @Override
    protected Node simplify(Node input0, Node input1) {
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        // TODO Auto-generated method stub
        return null;
    }

}
