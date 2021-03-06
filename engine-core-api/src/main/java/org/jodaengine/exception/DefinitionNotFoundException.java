package org.jodaengine.exception;

import javax.annotation.Nonnull;

import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * This is an exception stating that a definition does not exist.
 * 
 * @author Jan Rehwaldt
 */
public class DefinitionNotFoundException extends JodaEngineException {
    private static final long serialVersionUID = 5826993901901839412L;

    private static final String MESSAGE = "The requested process definition is not available or was removed.";

    private final ProcessDefinitionID definitionID;

    /**
     * Default Constructor.
     * 
     * @param definitionID
     *            the id of the resource that is not available
     */
    public DefinitionNotFoundException(@Nonnull ProcessDefinitionID definitionID) {

        super(MESSAGE);

        this.definitionID = definitionID;
    }

    /**
     * Gets the definition id.
     * 
     * @return the definition id
     */
    public ProcessDefinitionID getProcessDefinitionID() {

        return definitionID;
    }

    @Override
    @Nonnull
    public String toString() {

        return String.format("ProcessDefinition[id: %s]", getProcessDefinitionID());
    }
}
