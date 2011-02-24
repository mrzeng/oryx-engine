package de.hpi.oryxengine.routingBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Interface for the description of incoming and outgoing edges of a node.
 */
public interface RoutingBehaviour {

    /**
     * Execute the defined routing behavior. 
     * 
     * @param instance the process instance for which the list of succeeding instances should be returned
     * @return list list of succeeding process instances
     */
    List<ProcessInstance> execute(ProcessInstance instance);
}
