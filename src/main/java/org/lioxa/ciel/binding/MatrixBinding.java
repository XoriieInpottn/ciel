package org.lioxa.ciel.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;

/**
 * The {@link MatrixBinding} is used to bind a kind of real matrix
 * implementation to a variable.
 *
 * @author xi
 * @since Apr 1, 2016
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MatrixBinding {

    /**
     * Target class.
     *
     * @return The target class.
     */
    Class<? extends Node> target();

    /**
     * Matrix class.
     *
     * @return The matrix class.
     */
    Class<? extends RealMatrix> matrix();

    /**
     * The rating. <br/>
     * The default value is 10;
     *
     * @return The rating.
     */
    int rating() default 10;

}
