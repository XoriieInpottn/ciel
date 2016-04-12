package org.lioxa.ciel.simplifier;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class Simplifiers {

    static Map<Class<?>, Simplifier> map = new HashMap<>();

    public static Simplifier get(Class<? extends Simplifier> clazz) {
        Simplifier instance = map.get(clazz);
        if (instance == null) {
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                String msg = String.format("Failed to create simplifier %s.", clazz.getName());
                throw new RuntimeException(msg, e);
            }
            map.put(clazz, instance);
        }
        return instance;
    }

}
