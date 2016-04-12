package org.lioxa.ciel.operator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xi
 * @since Apr 12, 2016
 */
public class Operators {

    static Map<Class<?>, Operator> map = new HashMap<>();

    public static Operator get(Class<? extends Operator> clazz) {
        Operator operator = map.get(clazz);
        if (operator == null) {
            try {
                operator = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                String msg = String.format("Failed to instance operator \"%s\".", clazz.getName());
                throw new RuntimeException(msg, e);
            }
            map.put(clazz, operator);
        }
        return operator;
    }

}
