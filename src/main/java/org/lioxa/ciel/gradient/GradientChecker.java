package org.lioxa.ciel.gradient;

import org.lioxa.ciel.ExeTerm;
import org.lioxa.ciel.VarTerm;
import org.lioxa.ciel.matrix.MatrixUtils;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;

/**
 * {@link GradientChecker} is a tool aiming to simply check the correctness of
 * gradient.
 *
 * @author xi
 * @since May 24, 2016
 */
public class GradientChecker {

    VarTerm x;
    ExeTerm f;
    ExeTerm f1;
    double d;

    double fValue;
    RealMatrix mF1;

    /**
     * Create and setup.
     *
     * @param x
     *            The variable.
     * @param f
     *            The function.
     * @param f1
     *            The derivative of the function.
     * @param d
     *            The value of \delta which is close 0.
     */
    public GradientChecker(VarTerm x, ExeTerm f, ExeTerm f1, double d) {
        //
        // Check Parameters.
        if (!f.isScalar()) {
            throw new IllegalArgumentException("Function must be a scalar.");
        }
        if (d == 0) {
            throw new IllegalArgumentException("\"d\" cannot be zero.");
        }
        if (!f1.hasShape(x)) {
            throw new IllegalArgumentException("Derivative must have the same shape with \"x\".");
        }
        this.x = x;
        this.f = f;
        this.f1 = f1;
        this.d = d;
        this.mF1 = new RealMatrixImpl(f1.getRowSize(), f1.getColumnSize());
    }

    /**
     * Do gradient check.
     *
     * @param checkPoint
     *            The value which will be set to x in the check procedure.
     * @return The loss value. If the loss value is close to zero, that means
     *         our derivative is correct, or there may be problems with the
     *         gradient.
     */
    public double check(double checkPoint) {
        //
        // Initialize x.
        RealMatrix mX = this.x.getValue();
        int xRowSize = this.x.getRowSize();
        int xColSize = this.x.getColumnSize();
        for (int i = 0; i < xRowSize; i++) {
            for (int j = 0; j < xColSize; j++) {
                mX.set(i, j, checkPoint);
            }
        }
        this.x.setValue(mX);
        //
        // Compute f(x) and f1(x).
        this.fValue = this.f.execute().get(0, 0);
        MatrixUtils.copy(this.mF1, this.f1.execute());
        //
        // Compute loss for every elements of x.
        double loss = 0;
        for (int i = 0; i < xRowSize; i++) {
            for (int j = 0; j < xColSize; j++) {
                mX.set(i, j, checkPoint + this.d);
                this.x.setValue(mX);
                RealMatrix mFD = this.f.execute();
                double tmp = (mFD.get(0, 0) - this.fValue) / this.d - this.mF1.get(i, j);
                loss += tmp * tmp;
                mX.set(i, j, checkPoint);
            }
        }
        return loss;
    }

}
