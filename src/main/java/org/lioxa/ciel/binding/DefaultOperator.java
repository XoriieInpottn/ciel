package org.lioxa.ciel.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lioxa.ciel.operator.Operator;

/**
 *
 * @author xi
 * @since Apr 12, 2016
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefaultOperator {

    Class<? extends Operator> value();

}
