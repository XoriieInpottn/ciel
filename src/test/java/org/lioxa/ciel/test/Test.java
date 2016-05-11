package org.lioxa.ciel.test;

import java.util.Random;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.Executable;
import org.lioxa.ciel.Term;
import org.lioxa.ciel.VarTerm;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.matrix.impl.RealMatrixImpl;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.NodeUtils;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class Test {

    public static void main(String[] args) {
        Context context = new Context();
        VarTerm x = context.newVar(2, 2);
        VarTerm y = context.newVar(2, 2);
        Term r = context.dot(x, y);
        r = context.trans(r);
        r = context.trans(r);
        r = context.sum(r);
        Executable exe = context.build(r);
        //
        //
        Random rnd = new Random();
        RealMatrix mx = new RealMatrixImpl(2, 2);
        RealMatrix my = new RealMatrixImpl(2, 2);
        for (int i = 0; i < 3; i++) {
            double d;
            d = rnd.nextInt(10);
            mx.set(0, 0, d);
            d = rnd.nextInt(10);
            mx.set(1, 0, d);
            d = rnd.nextInt(10);
            mx.set(0, 1, d);
            d = rnd.nextInt(10);
            mx.set(1, 1, d);
            d = rnd.nextInt(10);
            my.set(0, 0, d);
            d = rnd.nextInt(10);
            my.set(1, 0, d);
            d = rnd.nextInt(10);
            my.set(0, 1, d);
            d = rnd.nextInt(10);
            my.set(1, 1, d);
            x.setValue(mx);
            y.setValue(my);
            System.out.println(mx);
            System.out.println(my);
            System.out.println(exe.execute());
        }
        System.out.println(mx);
        System.out.println(my);
        System.out.println(exe.execute());
        Node grad = context.diff((Node) r, (Node) x);
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        NodeUtils.printNodeTree(grad, 0);
        System.out.println("complete");
    }

}
