package org.lioxa.ciel.utils;

import org.lioxa.ciel.matrix.RealMatrix;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public class MatrixUtils {

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
