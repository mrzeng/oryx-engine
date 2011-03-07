package de.hpi.oryxengine.routing.behaviour.incoming.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.AbstractIncomingBehaviour;

/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour#joinable(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    @Override
    protected boolean joinable(ProcessInstance instance) {

        Node currentNode = instance.getCurrentNode();
        ProcessInstance parent = instance.getParentInstance();
        boolean joinable = true;

        List<ProcessInstance> childInstances = parent.getChildInstances();
        for (ProcessInstance child : childInstances) {
            if (child.getCurrentNode() != currentNode) {
                joinable = false;
            }
        }

        return joinable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour
     *      #performJoin(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    @Override
    protected List<ProcessInstance> performJoin(ProcessInstance instance) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<ProcessInstance> newInstances = new LinkedList<ProcessInstance>();
        Node currentNode = instance.getCurrentNode();
        ProcessInstance parent = instance.getParentInstance();
        parent.setCurrentNode(currentNode);
        newInstances.add(parent);
        return newInstances;
    }

}