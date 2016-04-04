package org.lioxa.ciel.binding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.operator.Operator;
import org.lioxa.ciel.utils.Reflects;

/**
 *
 * @author xi
 * @since Apr 2, 2016
 */
public class BindingManager {

    Map<Class<?>, List<Object[]>> bindings = new HashMap<>();

    /**
     *
     * @param binding
     * @param instance
     */
    public void addBinding(OperatorBinding binding, Operator instance) {
        Class<?> target = binding.target();
        Class<?>[] inputs = binding.inputs();
        Objects.requireNonNull(target);
        Objects.requireNonNull(inputs);
        if (inputs.length == 0) {
            throw new IllegalArgumentException("Inputs of binding cannot be empty.");
        }
        List<Object[]> lst = this.bindings.get(target);
        if (lst == null) {
            lst = new LinkedList<>();
            this.bindings.put(target, lst);
        }
        lst.add(new Object[] { binding, instance });
    }

    public Operator matchOperator(Class<? extends Node> target, Class<? extends RealMatrix>[] inputs) {
        List<Object[]> lst = this.bindings.get(target);
        if (lst == null) {
            return null;
        }
        int minDist = Integer.MAX_VALUE;
        OperatorBinding bestBinding = null;
        Operator bestInstance = null;
        for (Object[] tuple : lst) {
            OperatorBinding binding1 = (OperatorBinding) tuple[0];
            Class<? extends RealMatrix>[] inputs1 = binding1.inputs();
            if (inputs.length != inputs1.length) {
                continue;
            }
            int dist = 0;
            for (int i = 0; i < inputs.length; i++) {
                Class<?> input = inputs[i];
                Class<?> input1 = inputs1[i];
                int tmpDist = Reflects.distance(input, input1);
                if (tmpDist < 0) {
                    dist = -1;
                    break;
                }
            }
            if (dist < 0) {
                continue;
            }
            if (dist > minDist) {
                continue;
            }
            if (dist == minDist) {
                if (binding1.rating() <= bestBinding.rating()) {
                    continue;
                }
            }
            minDist = dist;
            bestInstance = (Operator) tuple[1];
            bestBinding = binding1;
        }
        return bestInstance;
    }

}
