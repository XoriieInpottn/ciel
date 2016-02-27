package org.lioxa.ciel.matrix;

/**
 *
 * @author xi
 * @since Mar 3, 2015
 */
public interface HasShape {

    int getRowSize();

    int getColumnSize();

    boolean isScalar();

    boolean isRowVector();

    boolean isColumnVector();

    boolean hasShape(HasShape hasShape);

    boolean hasShape(int rowSize, int colSize);

}
