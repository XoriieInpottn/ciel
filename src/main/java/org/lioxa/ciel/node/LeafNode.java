package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class LeafNode extends Node {

    @Override
    public RealMatrix execute() {
        return this.matrix;
    }

    @Override
    protected void initShape() {
        throw new UnsupportedOperationException("LeafNode cannot have any inputs.");
    }

}
