package org.lioxa.ciel.binding;

/**
 *
 * @author xi
 * @since Mar 11, 2016
 */
public class MatchResult {

    Binding binding;
    int distance;

    /**
     * Get binding.
     *
     * @return The binding.
     */
    public Binding getBinding() {
        return this.binding;
    }

    /**
     * Set binding.
     *
     * @param binding
     *            The binding.
     */
    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    /**
     * Get distance.
     *
     * @return The distance.
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Set distance.
     * 
     * @param distance
     *            The distance.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

}
