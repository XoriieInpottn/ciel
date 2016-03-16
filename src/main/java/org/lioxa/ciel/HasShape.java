package org.lioxa.ciel;

/**
 * The {@link HasShape} is used to describe a matrix like number. <br/>
 * Scalar, vector, matrix and even term have the {@link HasShape} interface.
 *
 * @author xi
 * @since Mar 3, 2015
 */
public interface HasShape {

    /**
     * Get the row size.
     *
     * @return The row size.
     */
    int getRowSize();

    /**
     * Get the column size.
     *
     * @return The column size.
     */
    int getColumnSize();

    /**
     * Is it a scalar?
     *
     * @return True if is a scalar, or false if not.
     */
    boolean isScalar();

    /**
     * Is it a row vector?
     *
     * @return True if is a row vector, or false if not.
     */
    boolean isRowVector();

    /**
     * Is it a column vector?
     *
     * @return True if is a column vector, or false if not.
     */
    boolean isColumnVector();

    /**
     * If it has the same shape as the given {@link HasShape}?
     *
     * @param hasShape
     *            Another {@link HasShape}.
     * @return True if they have the same shape, or false if not.
     */
    boolean hasShape(HasShape hasShape);

    /**
     * If it has the same shape as the given row size and column size?
     *
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size.
     * @return True if they have the same shape, or false if not.
     */
    boolean hasShape(int rowSize, int colSize);

}
