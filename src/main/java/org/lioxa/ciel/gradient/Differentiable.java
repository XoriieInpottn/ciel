package org.lioxa.ciel.gradient;

import org.lioxa.ciel.node.Node;

/**
 *
 * @author xi
 * @since May 11, 2016
 */
public interface Differentiable {

    /**
     * Compute derivative for "cost" with respect to a given node.
     *
     * @param cost
     *            The cost node. <br/>
     *            this.context node must be a scalar.
     * @param respectTo
     *            The node which the derivative is computed with respect to.
     * @return The derivative node.
     */
    Node diff(Node cost, Node respectTo);

    /**
     * Compute gradient for "cost" with respect to a given node.
     *
     * @param cost
     *            The cost node. <br/>
     *            this.context node must be a scalar.
     * @param respectTo
     *            The node which the gradient is computed with respect to.
     * @return The gradient node.
     */
    Node[] grad(Node cost, Node... respectTo);

}
