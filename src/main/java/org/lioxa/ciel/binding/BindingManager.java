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
public class BindingManager {

    Map<Class<?>, Map<Class<?>, BindingMatcher>> map = new HashMap<>();

    /**
     * Get the best operator instance for the given target and matrix.
     *
     * @param target
     *            The target class.
     * @param matrix
     *            The matrix class.
     * @return The operator instance.
     */
    public Operator getOperator(Class<? extends Node> target, Class<? extends RealMatrix> matrix) {
        //
        // First, the target class must be exactly the same.
        Map<Class<?>, BindingMatcher> matchers = this.map.get(target);
        if (matchers == null) {
            return null;
        }
        //
        // We, find the instance that best fit the given target and matrix.
        Operator bestInstance = null;
        int bestDist = Integer.MAX_VALUE;
        int bestRating = Integer.MIN_VALUE;
        for (Entry<Class<?>, BindingMatcher> entry : matchers.entrySet()) {
            Class<?> matrix0 = entry.getKey();
            BindingMatcher matcher = entry.getValue();
            int dist = Reflects.distance(matrix, matrix0);
            if (dist < bestDist) {
                //
                // We adopt the matrix class which has the minimum distance to
                // the given one. In this case, rating has no use.
                bestInstance = matcher.getInstance();
                bestDist = dist;
                bestRating = matcher.getRating();
            } else if (dist == bestDist) {
                //
                // Only if the two matrix classes have the same distance to the
                // given one, we compare their rating.
                int rating = matcher.getRating();
                if (rating > bestRating) {
                    bestInstance = matcher.getInstance();
                    bestDist = dist;
                    bestRating = rating;
                }
            }
        }
        return bestInstance;
    }

}
