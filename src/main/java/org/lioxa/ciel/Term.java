package org.lioxa.ciel;

import org.lioxa.ciel.matrix.HasShape;

/**
 * The {@link Term} is the basic element to form a expression. <br/>
 * The root {@link Term} must be compiled before execute it.
 *
 * @author xi
 * @since Feb 26, 2016
 */
public interface Term extends HasShape {

    Context getContext();

    int getInputSize();

    Term getInput(int index);

}
