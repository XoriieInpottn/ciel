package org.lioxa.ciel.matrix.impl;

import org.lioxa.ciel.HasShape;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link RealMatrixImpl} class is the default implementation of
 * {@link RealMatrix}. <br/>
 * It is the default Java implementation of matrix.
 *
 * @author xi
 * @since Mar 4, 2015
 */
public class RealMatrixImpl implements RealMatrix {

    final double[][] values;

    /**
     * Construct from shape. <br/>
     * The elements are filled with 0.
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     */
    public RealMatrixImpl(int rowSize, int colSize) {
        this.values = new double[rowSize][colSize];
    }

    //
    // RealMatrix interface.
    //

    @Override
    public int getRowSize() {
        return this.values.length;
    }

    @Override
    public int getColumnSize() {
        return this.values[0].length;
    }

    @Override
    public boolean isScalar() {
        return this.values.length == 1 && this.values[0].length == 1;
    }

    @Override
    public boolean isRowVector() {
        return this.values.length == 1;
    }

    @Override
    public boolean isColumnVector() {
        return this.values[0].length == 1;
    }

    @Override
    public boolean hasShape(HasShape hasShape) {
        return this.values.length == hasShape.getRowSize() && this.values[0].length == hasShape.getColumnSize();
    }

    @Override
    public boolean hasShape(int rowSize, int colSize) {
        return this.values.length == rowSize && this.values[0].length == colSize;
    }

    @Override
    public double get(int row, int col) {
        return this.values[row][col];
    }

    @Override
    public void set(int row, int col, double value) {
        this.values[row][col] = value;
    }

    @Override
    public void set(double value) {
        for (int i = 0; i < this.values.length; i++) {
            double[] row = this.values[i];
            for (int j = 0; j < row.length; j++) {
                row[j] = value;
            }
        }
    }

    @Override
    public void set(RealMatrix value) {
        for (int i = 0; i < this.values.length; i++) {
            double[] row = this.values[i];
            for (int j = 0; j < row.length; j++) {
                row[j] = value.get(i, j);
            }
        }
    }

    //
    // Convert to string.
    //

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.values.length; i++) {
            double[] row = this.values[i];
            sb.append('[');
            for (int j = 0; j < row.length; j++) {
                sb.append(' ');
                sb.append(Double.toString(row[j]));
            }
            sb.append(' ');
            sb.append(']');
            sb.append('\n');
        }
        return sb.toString();
    }

}
