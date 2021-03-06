package org.jodaengine.process.instance;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.util.Identifiable;

/**
 * The Interface ProcessInstance that represents what you actually call a process instance. It has several execution
 * threads, represented as tokens and a context that holds variables.
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractProcessInstance implements Identifiable<UUID> {

    /**
     * Assigns the token to this instance.
     * 
     * @param t
     *            the Token
     */
    public abstract void addToken(Token t);

    /**
     * Removes a token from the instance, for example, if it has finished execution.
     * 
     * @param token
     *            the Token
     */
    public abstract void removeToken(Token token);

    /**
     * Gets the instance context that is shared among all tokens that belong to this instance.
     * 
     * @return the context
     */
    @JsonProperty
    public abstract ProcessInstanceContext getContext();

    /**
     * Gets the tokens that belong to this instance.
     * 
     * @return the tokens
     */
    @JsonManagedReference
    public abstract List<Token> getAssignedTokens();

    /**
     * Gets the definition this instance was created for.
     * 
     * @return the definition
     */
    @JsonProperty
    public abstract ProcessDefinition getDefinition();

    /**
     * Checks if there tokens assigned to this instance. This indicates, that this instance is still running. If this
     * evaluates to false, this does not necessarily meant that this instance has finished, as it might not have been
     * started yet.
     * 
     * @return true, if there are tokens assigned
     */
    public abstract boolean hasAssignedTokens();

    /**
     * Cancel the execution of all tokens belonging to this instance.
     */
    public abstract void cancel();

    /**
     * Checks if this instance was cancelled.
     * 
     * @return true, if is cancelled
     */
    @JsonProperty
    public abstract boolean isCancelled();
    
    /**
     * Creates a new token pointing to the given node n in the same process instance context without
     * a parent token.
     *
     * @param startNode the start node
     * @return the new process token
     */
    public abstract Token createToken(Node startNode);

    /**
     * Creates a new token pointing to the given node n in the same process instance context.
     *
     * @param startNode the start node
     * @param parentToken the parent token
     * @return the new process token
     */
    public abstract Token createToken(Node startNode, Token parentToken);
    
    /**
     * Gets the builder.
     *
     * @return the builder
     */
    @JsonIgnore
    public abstract TokenBuilder getBuilder();
    
    /**
     * Sets the builder.
     *
     * @param builder the new builder
     */
    @JsonIgnore
    public abstract void setBuilder(TokenBuilder builder);

}
