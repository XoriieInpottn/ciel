package org.lioxa.ciel;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link Executable} is an interface that means a node is already build and
 * can be then executed. <br/>
 * Every expression can be built by its context. Then, the expression structure
 * is optimized(Expressions which have the same structure are merged). At the
 * same time, every node is filled with an operator and a result matrix.
 *
 * @author xi
 * @since Feb 27, 2016
 */
public interface Executable {

    /**
     * Execute the expression and get the calculated value.
     *
     * @return The result matrix.
     */
    RealMatrix execute();

}
