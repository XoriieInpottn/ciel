package org.lioxa.ciel.simplifier;

import org.lioxa.ciel.node.InternalNode;
import org.lioxa.ciel.node.Node;

/**
 *
 * @author xi
 * @since Apr 7, 2016
 */
public interface Simplifier {

    Node simplify(InternalNode node);

}
