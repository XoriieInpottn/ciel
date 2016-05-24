package org.lioxa.ciel.node;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.ExeTerm;
import org.lioxa.ciel.HasShape;
import org.lioxa.ciel.Term;
import org.lioxa.ciel.matrix.HasMatrix;
import org.lioxa.ciel.matrix.RealMatrix;

/**
 * The {@link Node} class is the default implementation of {@link ExeTerm}.
 *
 * @author xi
 * @since Feb 26, 2016
 */
public abstract class Node implements Term, HasMatrix, ExeTerm {

    //
    // Context.
    //

    protected Context context;

    @Override
    public Context getContext() {
        return this.context;
    }

    /**
     * Set context.
     *
     * @param context
     *            The context to be set.
     */
    public void setContext(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Context has been set.");
        }
        this.context = context;
    }

    //
    // Graph structure.
    //

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
     * Get output nodes.
     *
     * @return The set of output nodes.
     */
    public Set<Node> getOutputs() {
        return this.outputs.keySet();
    }

    //
    // Shape.
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
    // Simplify.
    //

    /**
     * Simplify the structure of this node based on the corresponding
     * computational feature. <br />
     * The default result is "this", which means does no simplify.
     *
     * @return The simplified node.
     */
    public Node simplify() {
        return this;
    }

    //
    // Matrix.
    //

    /**
     * The result matrix is created default by the operator when build the node.
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
    // Build.
    //

    /**
     * If this node has been build.
     *
     * @return True if this node has been build or false if not.
     */
    public boolean isBuild() {
        return this.matrix != null;
    }

    /**
     * Build the node tree rooted with this node.
     */
    public abstract void build();

    //
    // Execution.
    //

    protected boolean isExpired = true;

    @Override
    public boolean isExpired() {
        return this.isExpired;
    }

    @Override
    public void setExpired() {
        if (this.isExpired) {
            return;
        }
        this.isExpired = true;
        for (Node output : this.outputs.keySet()) {
            output.setExpired();
        }
    }

    //
    // Misc.
    //

    @Override
    public String toString() {
        String shape = String.format("(%d, %d)", this.rowSize, this.colSize);
        return String.format("%s: %s", this.getClass().getName(), shape);
    }

}
