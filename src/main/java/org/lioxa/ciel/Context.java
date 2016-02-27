package org.lioxa.ciel;

import org.lioxa.ciel.node.Node;

/**
 *
 * @author xi
 * @since Sep 25, 2015
 */
public class Context {

    public Executable compile(Term term) {
        Node node = (Node) term;
        if (node.getOperator() != null) {
            return node;
        }
        int inputSize = term.getInputSize();
        for (int i = 0; i < inputSize; i++) {
            Node input = (Node) term.getInput(i);
            this.compile(input);
        }
        return node;
    }

}
