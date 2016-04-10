package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.utils.MatrixUtils;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class ConstNode extends LeafNode {

    RealMatrix value;

    public RealMatrix getValue() {
        return this.value;
    }

    public ConstNode(RealMatrix value) {
        this.value = value;
        this.rowSize = value.getRowSize();
        this.colSize = value.getColumnSize();
    }

    @Override
    public void build() {
        super.build();
        MatrixUtils.copy(this.matrix, this.value);
    }

    @Override
    public Node diff(Node diff, Node respectTo) {
        throw new UnsupportedOperationException();
    }

}
