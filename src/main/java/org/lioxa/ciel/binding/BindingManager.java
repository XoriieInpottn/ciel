package org.lioxa.ciel.binding;

import java.util.HashMap;
import java.util.Map;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;

/**
 *
 * @author xi
 * @since Mar 9, 2016
 */
public class BindingManager {

    Map<Class<?>, Map<Class<?>, BindingMatcher>> map = new HashMap<>();

    public Operator getOperator(Class<? extends Node> target, Class<? extends RealMatrix> matrix) {
        Map<Class<?>, BindingMatcher> matchers = this.map.get(target);
        if (matchers == null) {
            return null;
        }
        //
        //
        return null;
    }
}
