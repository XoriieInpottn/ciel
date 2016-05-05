package org.lioxa.ciel;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 27, 2016
 */
public interface ConstTerm extends Term {

    /**
     * Get Value.
     *
     * @return The constant's value.
     */
    RealMatrix getValue();

}
