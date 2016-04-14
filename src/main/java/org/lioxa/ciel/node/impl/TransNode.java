package org.lioxa.ciel.node.impl;

import org.lioxa.ciel.binding.DefaultOperator;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.UnaryNode;
import org.lioxa.ciel.operator.impl.TransOperator;

/**
 *
 * @author xi
 * @since Apr 14, 2016
 */
@DefaultOperator(TransOperator.class)
public class TransNode extends UnaryNode {

    @Override
    protected void initShape(Node input0) {
        this.rowSize = input0.getColumnSize();
        this.colSize = input0.getRowSize();
    }

    @Override
    protected Node simplify(Node input0) {
        if (input0 instanceof TransNode) {
            return (Node) input0.getInput(0);
        }
        return this;
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        throw new UnsupportedOperationException();
    }

}
