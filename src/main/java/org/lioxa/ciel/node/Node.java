package org.lioxa.ciel.node;

import java.util.Map;
import java.util.WeakHashMap;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.Executable;
import org.lioxa.ciel.Term;
import org.lioxa.ciel.matrix.HasShape;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.operator.Operator;

/**
 * The {@link Node} class is the default implementation of {@link Executable}.
 *
 * @author xi
 * @since Feb 26, 2016
 */
public abstract class Node implements Executable {

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
    // Context.
    //

    protected Context context;

    @Override
    public Context getContext() {
        return this.context;
    }

    //
    // Structure.
    //

    protected Node[] inputs;
    protected RealMatrix[] inputMatrices;
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
        this.inputs = inputs.clone();
        this.inputMatrices = new RealMatrix[this.inputs.length];
        for (Node input : this.inputs) {
            input.outputs.put(this, null);
        }
    }

    //
    //
    //

    protected Operator operator;
    protected RealMatrix matrix;

    /**
     * Get this node's operator.
     *
     * @return The operator there is one, or null will be return;
     */
    public Operator getOperator() {
        return this.operator;
    }

    /**
     * Set an operator to this node. <br/>
     * At the same time, a result matrix is created by the operator. <br/>
     * Note that the result matrix must be created by an specific operator since
     * the matrix implementation can be <b>deeply depends</b> on the organize of
     * the operator. Moreover, the operator can be set only once at the initial
     * stage, or a runtime exception will be thrown.
     *
     * @param operator
     *            The operator.
     */
    public void setOperator(Operator operator) {
        if (this.operator != null) {
            throw new IllegalStateException("Operator has been set.");
        }
        this.operator = operator;
        this.matrix = this.operator.createMatrix(this.rowSize, this.colSize);
    }

    /**
     * Get the result matrix. <br/>
     * The result matrix is created by the operator when setting operator.
     *
     * @return The result matrix.
     */
    public RealMatrix getMatrix() {
        return this.matrix;
    }

    //
    //
    //

    protected boolean isExpired = true;

    /**
     * Is the value of result matrix expired? <br/>
     * If not, there is no need to execute this node. In many cases, it will
     * help to save a lot of time.
     *
     * @return True if the value expired, false if not.
     */
    public boolean isExpired() {
        return this.isExpired;
    }

    /**
     * Set the expired to "true" by force.
     */
    public void setExpired() {
        this.isExpired = true;
    }

    @Override
    public RealMatrix execute() {
        for (int i = 0; i < this.inputs.length; i++) {
            if (this.inputs[i].isExpired) {
                this.inputs[i].execute();
            }
            this.inputMatrices[i] = this.inputs[i].matrix;
        }
        this.operator.execute(this.matrix, this.inputMatrices);
        return this.matrix;
    }

}
