package org.lioxa.ciel.matrix;

/**
 * The {@link HasMatrix} interface is used to describe objects that contain a
 * matrix. <br/>
 * This kind of objects must be initialized with a matrix before using.
 *
 * @author xi
 * @since Mar 15, 2016
 */
public interface HasMatrix {

    /**
     * Get matrix.
     *
     * @return The matrix.
     */
    RealMatrix getMatrix();

    /**
     * Set matrix.
     * 
     * @param matrix
     *            The matrix.
     */
    void setMatrix(RealMatrix matrix);

}
