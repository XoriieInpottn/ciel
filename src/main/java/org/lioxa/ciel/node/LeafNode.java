package org.lioxa.ciel.node;

import java.lang.reflect.Constructor;

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
        Class<? extends RealMatrix> matrixClass = this.context.getDefaultMatrixClass();
        try {
            Constructor<? extends RealMatrix> c;
            //
            // This requires all implementations of RealMatrix have a
            // constructor like "constructor(int rowSize, int colSize)".
            c = matrixClass.getConstructor(int.class, int.class);
            this.matrix = c.newInstance(this.rowSize, this.colSize);
        } catch (Exception e) {
            String matrixName = matrixClass.getName();
            String nodeName = this.getClass().getName();
            String msg = String.format("Failed to create instance of %s for %s.", matrixName, nodeName);
            throw new RuntimeException(msg, e);
        }
    }

}
