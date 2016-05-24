package org.lioxa.ciel.matrix;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.lioxa.ciel.binding.OperatorBinding;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.operator.Operator;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class MatrixUtils {

    public static RealMatrix createByOperator(Class<? extends Operator> clazz, int rowSize, int colSize) {
        OperatorBinding binding = clazz.getAnnotation(OperatorBinding.class);
        return createByClass(binding.output(), rowSize, colSize);
    }

    /**
     * Create instance for implementation of {@link RealMatrix}.
     *
     * @param clazz
     *            The matrix class.
     * @param rowSize
     *            The row size.
     * @param colSize
     *            The column size;
     * @return The matrix instance.
     */
    public static RealMatrix createByClass(Class<? extends RealMatrix> clazz, int rowSize, int colSize) {
        try {
            Constructor<? extends RealMatrix> c = clazz.getConstructor(int.class, int.class);
            return c.newInstance(rowSize, colSize);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            String msg = String.format("Failed to create instance for %s.", clazz.getName());
            throw new RuntimeException(msg, e);
        }
    }

    public static RealMatrixImpl copy(RealMatrix src) {
        RealMatrixImpl dst = new RealMatrixImpl(src.getRowSize(), src.getColumnSize());
        copy(dst, src);
        return dst;
    }

    public static void copy(RealMatrix dst, RealMatrix src) {
        int rowSize = dst.getRowSize();
        int colSize = dst.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                dst.set(i, j, src.get(i, j));
            }
        }
    }

    public static boolean isAllOne(RealMatrix matrix) {
        return isAllValue(matrix, 1);
    }

    public static boolean isAllZero(RealMatrix matrix) {
        return isAllValue(matrix, 0);
    }

    public static boolean isAllValue(RealMatrix matrix, double value) {
        int rowSize = matrix.getRowSize();
        int colSize = matrix.getColumnSize();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (value != matrix.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

}
