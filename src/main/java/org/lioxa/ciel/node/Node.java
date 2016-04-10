package org.lioxa.ciel.node;

import java.util.Map;
import java.util.Set;
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

    public void setContext(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Context has been set.");
        }
        this.context = context;
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

    public Set<Node> getOutputs() {
        return this.outputs.keySet();
    }

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

    //
    // Build.
    //

    public abstract void build();

    //
    // Differentiation.
    //

    public abstract Node diff(Node diff, Node respectTo);

}
