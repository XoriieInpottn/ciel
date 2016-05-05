package org.lioxa.ciel.node;

import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public abstract class LeafNode extends Node {

    public LeafNode() {
        this.inputs = new Node[0];
    }

    @Override
    public RealMatrix execute() {
        this.isExpired = false;
        return this.matrix;
    }

    @Override
    public void build() {
        if (this.matrix != null) {
            //
            // In this situation, the matrix has been set manually.
            return;
        }
        //
        // Usually, the matrix is created automatically.
        //
        // This requires all implementations of RealMatrix have a
        // constructor like "constructor(int rowSize, int colSize)".
        Class<? extends RealMatrix> matrixClass = this.context.getDefaultMatrixClass();
        this.matrix = MatrixUtils.createByClass(matrixClass, this.rowSize, this.colSize);
    }

}
