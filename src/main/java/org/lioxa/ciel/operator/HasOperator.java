package org.lioxa.ciel.operator;

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
     * By default, the operator of a node is null and it will be set
     * automatically during the build stage. But in some special cases, like
     * when user defines a custom operator, it need to be set manually.
     * 
     * @param operatorType
     *            The operator type.
     */
    void setOperator(Class<? extends Operator> operatorType);

    /**
     * Set an operator to this node. <br/>
     *
     *
     * @param operator
     *            The operator.
     */
    void setOperator(Operator operator);

}
