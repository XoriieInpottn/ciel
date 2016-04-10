package org.lioxa.ciel.test;

import org.lioxa.ciel.Context;
import org.lioxa.ciel.Executable;
import org.lioxa.ciel.matrix.RealMatrix;
import org.lioxa.ciel.node.Node;
import org.lioxa.ciel.node.impl.AddNode;
import org.lioxa.ciel.utils.NodeUtils;

/**
 *
 * @author xi
 * @since Apr 4, 2016
 */
public class Test {

    public static void main(String[] args) {
        Context context = new Context();
        Node x = context.newVar(2, 2);
        Node y = context.newZero(2, 2);
        Node sum = context.newOpt(AddNode.class, x, y);
        NodeUtils.printNodeTree(sum, 0);
        Executable exe = context.build(sum);
        x.getMatrix().set(0, 1, 2);
        x.getMatrix().set(0, 0, 1);
        RealMatrix r = exe.execute();
        System.out.println(x.getMatrix());
        System.out.println(y.getMatrix());
        System.out.println(r);
        System.out.println("complete");
    }

}
