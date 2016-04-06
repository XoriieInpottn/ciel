package org.lioxa.ciel.matrix.impl;

import org.lioxa.ciel.HasShape;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 6, 2016
 */
public class SingleValueMatrix implements RealMatrix {

    int rowSize;
    int colSize;
    double value;

    public SingleValueMatrix(int rowSize, int colSize, double value) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.value = value;
    }

    @Override
    public int getRowSize() {
        return this.rowSize;
    }

    @Override
    public int getColumnSize() {
        return this.colSize;
    }

    @Override
    public boolean isScalar() {
        return this.rowSize == 1 && this.colSize == 1;
    }

    @Override
    public boolean isRowVector() {
        return this.rowSize == 1;
    }

    @Override
    public boolean isColumnVector() {
        return this.colSize == 1;
    }

    @Override
    public boolean hasShape(HasShape hasShape) {
        return this.rowSize == hasShape.getRowSize() && this.colSize == hasShape.getColumnSize();
    }

    @Override
    public boolean hasShape(int rowSize, int colSize) {
        return this.rowSize == rowSize && this.colSize == colSize;
    }

    @Override
    public double get(int row, int col) {
        if (row >= this.rowSize || col >= this.colSize) {
            throw new IndexOutOfBoundsException();
        }
        return 1;
    }

    @Override
    public void set(int row, int col, double value) {
        throw new UnsupportedOperationException("Cannot set value to SingleValueMatrix.");
    }

    @Override
    public void set(double value) {
        this.value = value;
    }

    //
    // Convert to string.
    //

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rowSize; i++) {
            sb.append('[');
            for (int j = 0; j < this.colSize; j++) {
                sb.append(' ');
                sb.append(Double.toString(this.value));
            }
            sb.append(' ');
            sb.append(']');
            sb.append('\n');
        }
        return sb.toString();
    }

}
