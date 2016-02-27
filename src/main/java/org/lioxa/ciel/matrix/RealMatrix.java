package org.lioxa.ciel.matrix;

/**
 *
 * @author xi
 * @since Mar 4, 2015
 */
public interface RealMatrix extends HasShape {

    double get(int row, int col);

    void set(int row, int col, double value);

    void set(double value);

}
