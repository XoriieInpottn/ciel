package org.lioxa.ciel.binding;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;

/**
 * The {@link BindingMatcher} is used to describe the binding information.
 *
 * @author xi
 * @since Mar 9, 2016
 */
public class BindingMatcher {

    Class<? extends Node> target;
    Class<? extends RealMatrix> matrix;
    int rating;
    Operator instance;

    /**
     * Get the target class.
     *
     * @return The target class.
     */
    public Class<? extends Node> getTarget() {
        return this.target;
    }

    /**
     * Set the target class.
     *
     * @param target
     *            The target class.
     */
    public void setTarget(Class<? extends Node> target) {
        this.target = target;
    }

    /**
     * Get the matrix class.
     *
     * @return The matrix class.
     */
    public Class<? extends RealMatrix> getMatrix() {
        return this.matrix;
    }

    /**
     * Set the matrix class.
     *
     * @param matrix
     *            The matrix class.
     */
    public void setMatrix(Class<? extends RealMatrix> matrix) {
        this.matrix = matrix;
    }

    /**
     * Get the rating.
     *
     * @return The rating.
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Set the rating.
     *
     * @param rating
     *            The rating.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Get the operator instance.
     *
     * @return The operator instance.
     */
    public Operator getInstance() {
        return this.instance;
    }

    /**
     * Set the operator instance.
     * 
     * @param instance
     *            The operator instance.
     */
    public void setInstance(Operator instance) {
        this.instance = instance;
    }

}
