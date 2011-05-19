package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing transition, of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split according to the transitions.
     * 
     * @param instances
     *            the instances
     * @return the list
     * @throws NoValidPathException
     *             the no valid path exception {@inheritDoc}
     */
    @Override
    public List<Token> split(List<Token> instances)
    throws NoValidPathException {

        if (instances.size() == 0) {
            return instances;
        }

        List<Transition> transitionList = new ArrayList<Transition>();
        List<Token> transitionsToNavigate = null;

        // we look through the outgoing transitions and try to find one at least, whose condition evaluates true and
        // then return it as the to-be-taken transition
        for (Token instance : instances) {
            Node currentNode = instance.getCurrentNode();
            for (Transition transition : currentNode.getOutgoingTransitions()) {
                if (transition.getCondition().evaluate(instance)) {
                    transitionList.add(transition);
                    break;
                }
            }

            if (transitionList.size() == 0) {

                throw new NoValidPathException();

            }

            transitionsToNavigate = instance.navigateTo(transitionList);

        }
        return transitionsToNavigate;
    }

}