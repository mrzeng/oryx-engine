package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2 add Number activities.
 */
public class ExampleProcessDeployer extends AbstractProcessDeployer {

    /** The node1. */
    private Node node1;

    /** The node2. */
    private Node node2;

    /** The start node. */
    private Node startNode;

    /**
     * Instantiates a new example process token factory.
     */
    public ExampleProcessDeployer() {

        builder = new ProcessDefinitionBuilderImpl();
    }

    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        int[] ints = {1, 1};
        node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);
        node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);
        
        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);
        
        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, endNode);
    }

}
