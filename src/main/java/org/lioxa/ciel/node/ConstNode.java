package org.lioxa.ciel.node;

import org.lioxa.ciel.ConstTerm;
import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 * {@link ConstNode}. <br />
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class ConstNode extends LeafNode implements ConstTerm {

    RealMatrix value;

    //
    // Value.
    //

    @Override
    public RealMatrix getValue() {
        return this.value;
    }

    public ConstNode(RealMatrix value) {
        this.value = value;
        this.rowSize = value.getRowSize();
        this.colSize = value.getColumnSize();
    }

    //
    // Build.
    //

    @Override
    public void build() {
        super.build();
        MatrixUtils.copy(this.matrix, this.value);
    }

}
