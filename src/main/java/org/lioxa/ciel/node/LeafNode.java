package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class LeafNode extends Node {

    @Override
    public void setInputs(Node[] inputs) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void initShape() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RealMatrix execute() {
        return this.matrix;
    }

}
