package org.lioxa.ciel.node;

import org.lioxa.ciel.ConstTerm;
import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class ConstNode extends LeafNode implements ConstTerm {

    RealMatrix value;

    @Override
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

}
