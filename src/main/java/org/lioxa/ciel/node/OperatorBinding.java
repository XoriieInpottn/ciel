package org.lioxa.ciel.node;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Oct 25, 2015
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OperatorBinding {

    Class<? extends Node> target();

    Class<? extends RealMatrix> matrix();

    int rating() default 10;

}
