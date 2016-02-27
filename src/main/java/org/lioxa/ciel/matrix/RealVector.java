package org.lioxa.ciel.matrix;

/**
 *
 * @author xi
 * @since Jun 15, 2015
 */
public interface RealVector extends RealMatrix {

    int getSize();

    double get(int index);

    void set(int index, double value);

}
