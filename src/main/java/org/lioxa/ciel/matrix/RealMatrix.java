package org.lioxa.ciel.matrix;

import org.lioxa.ciel.HasShape;

/**
 * The {@link RealMatrix} is used to describe matrices with double values. <br/>
 * Scalars, vectors can be also regarded as matrices.
 *
 * @author xi
 * @since Mar 4, 2015
 */
public interface RealMatrix extends HasShape {

    /**
     * Get value in the specific position.
     *
     * @param row
     *            The row number.
     * @param col
     *            The column number.
     * @return The value.
     */
    double get(int row, int col);

    /**
     * Set value to the specific position.
     *
     * @param row
     *            The row number.
     * @param col
     *            The column number.
     * @param value
     *            The value to be set.
     */
    void set(int row, int col, double value);

    /**
     * Set value to all positions.
     *
     * @param value
     *            The value to be set.
     */
    void set(double value);

    void set(RealMatrix value);

}
