package org.lioxa.ciel;

import org.lioxa.ciel.operator.Operator;

/**
 * The {@link HasOperator} interface describe a series of nodes which can
 * perform an operation on their input nodes. <br/>
 * This kind of nodes must be initialized with an operator when building the
 * whole expression tree.
 *
 * @author xi
 * @since Mar 15, 2016
 */
public interface HasOperator {

    /**
     * Get this node's operator.
     *
     * @return The operator there is one, or null will be return;
     */
    Operator getOperator();

    /**
     * Set an operator to this node. <br/>
     * At the same time, a result matrix is created by the operator. <br/>
     * Note that the result matrix must be created by an specific operator since
     * the matrix implementation can be <b>deeply depends</b> on the organize of
     * the operator. Moreover, the operator can be set only once at the initial
     * stage, or a runtime exception will be thrown.
     *
     * @param operator
     *            The operator.
     */
    // void setOperator(Operator operator);

}
