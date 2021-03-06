package org.lioxa.ciel.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;

/**
 * The {@link OperatorBinding} annotation is used to bind an operator to a kind
 * of node with a specific matrix type.
 *
 * @author xi
 * @since Oct 25, 2015
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OperatorBinding {

    /**
     * Target class.
     *
     * @return The target class.
     */
    Class<? extends Node> target();

    /**
     * input matrix classes.
     *
     * @return The matrix classes.
     */
    Class<? extends RealMatrix>[] inputs();

    /**
     * Output matrix class.
     *
     * @return The matrix class.
     */
    Class<? extends RealMatrix> output();

    /**
     * Rating. <br/>
     * The default value is 10;
     *
     * @return The rating.
     */
    int rating() default 10;

}
