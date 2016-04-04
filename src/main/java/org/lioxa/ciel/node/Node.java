package org.lioxa.ciel.node;

import java.util.Map;
import java.util.WeakHashMap;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.Executable;
import org.lioxa.ciel.HasMatrix;
import org.lioxa.ciel.HasShape;
import org.lioxa.ciel.Term;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link Node} class is the default implementation of {@link Executable}.
 *
 * @author xi
 * @since Feb 26, 2016
 */
public abstract class Node implements Term, HasMatrix, Executable {

    //
    // HasShape interface.
    //

    protected int rowSize;
    protected int colSize;

    @Override
    public int getRowSize() {
        return this.rowSize;
    }

    @Override
    public int getColumnSize() {
        return this.colSize;
    }

    @Override
    public boolean isScalar() {
        return this.rowSize == 1 && this.colSize == 1;
    }

    @Override
    public boolean isRowVector() {
        return this.rowSize == 1;
    }

    @Override
    public boolean isColumnVector() {
        return this.colSize == 1;
    }

    @Override
    public boolean hasShape(HasShape hasShape) {
        return this.rowSize == hasShape.getRowSize() && this.colSize == hasShape.getColumnSize();
    }

    @Override
    public boolean hasShape(int rowSize, int colSize) {
        return this.rowSize == rowSize && this.colSize == colSize;
    }

    //
    // Term interface.
    //

    protected Context context;

    @Override
    public Context getContext() {
        return this.context;
    }

    protected Node[] inputs;
    protected Map<Node, Object> outputs = new WeakHashMap<>();

    @Override
    public int getInputSize() {
        return this.inputs.length;
    }

    @Override
    public Term getInput(int index) {
        return this.inputs[index];
    }

    /**
     * Set all input nodes to this node. <br/>
     * At the same time, the output of the given nodes will be set to this node.
     * This method is very important, and it is the first step to initialize a
     * new node.
     *
     * @param inputs
     *            The input nodes.
     */
    public void setInputs(Node[] inputs) {
        if (this.inputs != null) {
            throw new IllegalStateException("Inputs has been set.");
        }
        this.inputs = inputs.clone();
        for (Node input : this.inputs) {
            input.outputs.put(this, null);
        }
        this.initShape();
    }

    /**
     * Initialize shape for the node.
     */
    protected abstract void initShape();

    //
    // HasMatrix interface.
    //

    /**
     * The result matrix is created by the operator when setting operator.
     */
    protected RealMatrix matrix;

    @Override
    public RealMatrix getMatrix() {
        return this.matrix;
    }

    @Override
    public void setMatrix(RealMatrix matrix) {
        if (this.matrix != null) {
            throw new IllegalStateException("Matrix has been set.");
        }
        this.matrix = matrix;
    }

    //
    // Executable interface.
    //

    protected boolean isExpired = true;

    @Override
    public boolean isExpired() {
        return this.isExpired;
    }

    @Override
    public void setExpired() {
        this.isExpired = true;
    }

}
