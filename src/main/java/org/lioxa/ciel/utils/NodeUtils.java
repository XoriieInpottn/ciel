package org.lioxa.ciel.utils;

import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.ConstNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.OneNode;
import org.lioxa.ciel.node.ZeroNode;

/**
 *
 * @author xi
 * @since Apr 9, 2016
 */
public class NodeUtils {

    /**
     * Is a constant node with its value "0".
     *
     * @param node
     * @return
     */
    public static boolean isAllZero(Node node) {
        if (!(node instanceof ConstNode)) {
            return false;
        }
        if (node instanceof ZeroNode) {
            return true;
        }
        RealMatrix value = ((ConstNode) node).getValue();
        return MatrixUtils.isAllZero(value);
    }

    /**
     * Is a constant node with its value "1".
     *
     * @param node
     * @return
     */
    public static boolean isAllOne(Node node) {
        if (!(node instanceof ConstNode)) {
            return false;
        }
        if (node instanceof OneNode) {
            return true;
        }
        RealMatrix value = ((ConstNode) node).getValue();
        return MatrixUtils.isAllOne(value);
    }

    /**
     * Print a node as a tree.
     *
     * @param node
     *            The root node.
     * @param level
     *            The intent level.
     */
    public static void printNodeTree(Node node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("|   ");
        }
        System.out.println(node);
        int inputSize = node.getInputSize();
        for (int i = 0; i < inputSize; i++) {
            Node input = (Node) node.getInput(i);
            printNodeTree(input, level + 1);
        }
    }

}
