package org.lioxa.ciel;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 27, 2016
 */
public interface VarTerm extends Term {

    /**
     * Get Value.
     *
     * @return The variable's value.
     */
    RealMatrix getValue();

    /**
     * Set value.
     * 
     * @param value
     *            The variable's new value.
     */
    void setValue(RealMatrix value);

}
