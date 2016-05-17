package org.lioxa.ciel.gradient;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.impl.AddNode;

/**
 *
 * @author xi
 * @since May 11, 2016
 */
public class Gradient implements Differentiable {

    Context context;

    public Gradient(Context context) {
        this.context = context;
    }

    @Override
    public Node diff(Node cost, Node respectTo) {
        //
        // Check context
        if (cost.getContext() != this.context || respectTo.getContext() != this.context) {
            throw new RuntimeException("Any of the nodes may not belong to this context.");
        }
        //
        // Get tree nodes.
        this.getTreeNodes(cost);
        //
        // If the node that with respect to is in the tree.
        if (!this.treeNodes.contains(respectTo)) {
            String msg = String.format("%s is not a component of %.", respectTo, cost);
            throw new RuntimeException(msg);
        }
        //
        // Get the derivative, clear the tree nodes set and return the result.
        Node diff = this.diff_r(cost, respectTo);
        this.treeNodes.clear();
        return diff;
    }

    @Override
    public Node[] grad(Node cost, Node... respectTo) {
        //
        // Check context
        if (cost.getContext() != this.context) {
            throw new RuntimeException("Any of the nodes may not belong to this context.");
        }
        for (Node respectTo1 : respectTo) {
            if (respectTo1.getContext() != this.context) {
                throw new RuntimeException("Any of the nodes may not belong to this context.");
            }
        }
        //
        // Get tree nodes.
        this.getTreeNodes(cost);
        //
        // If the node that with respect to is in the tree.
        for (Node respectTo1 : respectTo) {
            if (!this.treeNodes.contains(respectTo1)) {
                String msg = String.format("%s is not a component of %.", respectTo1, cost);
                throw new RuntimeException(msg);
            }
        }
        //
        // Get the derivative, clear the tree nodes set and return the result.
        Node[] grad = new Node[respectTo.length];
        for (int i = 0; i < respectTo.length; i++) {
            Node respectTo1 = respectTo[i];
            grad[i] = this.diff_r(cost, respectTo1);
        }
        this.treeNodes.clear();
        return grad;
    }

    Set<Node> treeNodes = new HashSet<>();

    /**
     * Get tree nodes for the given root.
     *
     * @param root
     *            The root node.
     * @param treeNodes
     *            The result collection of tree nodes.
     */
    void getTreeNodes(Node root) {
        this.treeNodes.add(root);
        int size = root.getInputSize();
        for (int i = 0; i < size; i++) {
            this.getTreeNodes((Node) root.getInput(i));
        }
    }

    /**
     * Get derivative for the given cost with respect to the given node.
     *
     * @param cost
     *            The cost node.
     * @param respectTo
     *            The node that with respect to.
     * @param treeNodes
     *            The tree nodes of cost node.
     * @return The derivative node.
     */
    Node diff_r(Node cost, Node respectTo) {
        if (respectTo.equals(cost)) {
            return this.context.oneNode(1, 1);
        }
        List<Node> parts = new LinkedList<>();
        for (Node output : respectTo.getOutputs()) {
            if (!this.treeNodes.contains(output)) {
                continue;
            }
            Node grad = this.diff_r(cost, output);
            grad = ((InternalNode) output).diff(grad, respectTo);
            parts.add(grad);
        }
        switch (parts.size()) {
        case 0:
            String msg = String.format("%s is not a component of %.", respectTo, cost);
            throw new RuntimeException(msg);
        case 1:
            return parts.get(0);
        default:
            Iterator<Node> iter = parts.iterator();
            iter.hasNext();
            Node grad = iter.next();
            while (iter.hasNext()) {
                Node grad1 = iter.next();
                grad = this.context.internalNode(AddNode.class, grad, grad1);
            }
            return grad;
        }
    }

}
