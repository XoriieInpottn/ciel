package org.lioxa.ciel;

/**
 * The {@link Term} is the basic element to form a expression. <br/>
 * The root {@link Term} must be compiled before execute it.
 *
 * @author xi
 * @since Feb 26, 2016
 */
public interface Term extends HasShape {

    /**
     * Get the context.
     *
     * @return The context.
     */
    Context getContext();

    /**
     * Get the number of the input {@link Term}.
     *
     * @return The number of the input.
     */
    int getInputSize();

    /**
     * Get the specific input {@link Term}(node).
     *
     * @param index
     *            The index.
     * @return The input {@link Term}.
     */
    Term getInput(int index);

}
