package org.lioxa.ciel.node;

import org.lioxa.ciel.VarTerm;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 * {@link VarNode}. <br />
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class VarNode extends LeafNode implements VarTerm {

    public VarNode(int rowSize, int colSize) {
        this.rowSize = rowSize;
        this.colSize = colSize;
    }

    //
    // Value.
    //

    @Override
    public RealMatrix getValue() {
        return this.matrix;
    }

    @Override
    public void setValue(RealMatrix value) {
        this.matrix.set(value);
        this.setExpired();
    }

}
