package de.hpi.oryxengine.process.definition;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl.
 */
public class ProcessDefinitionImpl implements ProcessDefinition {
    
    /** The description. */
    private String description;
    
    /** The id. */
    private UUID id;
    
    /** The start nodes. */
    private List<Node> startNodes;
    
    /**
     * Instantiates a new process definition. A UUID is generated randomly.
     *
     * @param id the process definition id
     * @param description the description
     * @param startNodes the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id, String description, List<Node> startNodes) {
        this.id = id;
        this.description = description;
        this.startNodes = startNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getID() {

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {

        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {

        this.description = description;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Node> getStartNodes() {

        return startNodes;
    }

}
