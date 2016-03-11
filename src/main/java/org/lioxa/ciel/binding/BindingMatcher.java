package org.lioxa.ciel.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.utils.Reflects;

/**
 *
 * @author xi
 * @since Mar 9, 2016
 */
public class BindingMatcher {

    Map<Class<?>, Map<Class<?>, Binding>> map = new HashMap<>();

    /**
     * Get the best binding for the given target and matrix.
     *
     * @param target
     *            The target class.
     * @param matrix
     *            The matrix class.
     * @return The operator instance.
     */
    public MatchResult matchOperator(Class<? extends Node> target, Class<? extends RealMatrix> matrix) {
        //
        // First, the target class must be exactly the same.
        Map<Class<?>, Binding> bindings = this.map.get(target);
        if (bindings == null) {
            return null;
        }
        //
        // We, find the instance that best fit the given target and matrix.
        Binding bestBinding = null;
        int bestDist = Integer.MAX_VALUE;
        for (Entry<Class<?>, Binding> entry : bindings.entrySet()) {
            Class<?> matrix0 = entry.getKey();
            Binding binding = entry.getValue();
            int dist = Reflects.distance(matrix, matrix0);
            if (dist < bestDist) {
                //
                // We adopt the matrix class which has the minimum distance to
                // the given one. In this case, rating is not used.
                bestBinding = binding;
                bestDist = dist;
            } else if (dist == bestDist) {
                //
                // Only if the two matrix classes have the same distance to the
                // given one, we compare their rating.
                if (bestBinding != null && binding.getRating() > bestBinding.getRating()) {
                    bestBinding = binding;
                }
            }
        }
        MatchResult result = new MatchResult();
        result.setBinding(bestBinding);
        result.setDistance(bestDist);
        return result;
    }

    /**
     * Bind an operator to this binding matcher.
     *
     * @param target
     *            The target class.
     * @param matrix
     *            The matrix class.
     * @param rating
     *            The rating.
     * @param instance
     *            The operator instance.
     */
    public void bind(Class<? extends Node> target, Class<? extends RealMatrix> matrix, int rating, Operator instance) {
        Map<Class<?>, Binding> bindings = this.map.get(target);
        if (bindings == null) {
            bindings = new HashMap<>();
            this.map.put(target, bindings);
        }
        Binding binding = bindings.get(matrix);
        if (binding != null && rating < binding.getRating()) {
            //
            // Only if the given rating smaller than the existing one, the
            // existing binding not be replaced.
            return;
        }
        //
        // Create a new binding or replace the existing one.
        binding = new Binding();
        bindings.put(matrix, binding);
        binding.setTarget(target);
        binding.setMatrix(matrix);
        binding.setRating(rating);
        binding.setInstance(instance);
    }

}
