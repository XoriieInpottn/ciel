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
     * Add an operator binding to the manager.
     *
     * @param binding
     *            The operator binding annotation.
     * @param operatorClass
     *            The operator class.
     */
    public void addOperatorBinding(OperatorBinding binding, Class<? extends Operator> operatorClass) {
        //
        // Check arguments.
        Class<?> target = binding.target();
        Class<?>[] inputs = binding.inputs();
        Objects.requireNonNull(target);
        Objects.requireNonNull(inputs);
        if (inputs.length == 0) {
            throw new IllegalArgumentException("\"inputs\" of binding annotation cannot be empty.");
        }
        //
        // Get binding list for the given target.
        List<Object[]> lst = this.bindings.get(target);
        if (lst == null) {
            lst = new LinkedList<>();
            this.bindings.put(target, lst);
        }
        //
        // Add (binding, instance) tuple to the list.
        lst.add(new Object[] { binding, operatorClass });
    }

    /**
     * Matcher operator with target and inputs.
     *
     * @param target
     *            The target of the operator.
     * @param inputs
     *            The input types of the operator.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Operator> matchOperator(Class<? extends Node> target, Class<? extends RealMatrix>[] inputs) {
        //
        // Get the binding list.
        List<Object[]> lst = this.bindings.get(target);
        if (lst == null) {
            return null;
        }
        //
        // Start matching.
        int minDist = Integer.MAX_VALUE;
        OperatorBinding bestBinding = null;
        Class<? extends Operator> bestClass = null;
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
            bestClass = (Class<? extends Operator>) tuple[1];
            bestBinding = binding1;
        }
        return bestClass;
    }

}
