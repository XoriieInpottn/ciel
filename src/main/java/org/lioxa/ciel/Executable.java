package org.lioxa.ciel;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Feb 27, 2016
 */
public interface Executable extends Term {

    RealMatrix execute();

}
