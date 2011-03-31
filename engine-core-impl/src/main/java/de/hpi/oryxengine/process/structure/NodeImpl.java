package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    private Activity activity;

    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    private List<Transition> outgoingTransitions, incomingTransitions;

    private UUID id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param activity
     *            the activity to be executed
     * @param incomingBehaviour
     *            the incoming behaviour
     * @param outgoingBehaviour
     *            the outgoing behaviour
     * @param event
     *            the event for the node, e.g. a timer event
     */
    public NodeImpl(Activity activity,
                    IncomingBehaviour incomingBehaviour,
                    OutgoingBehaviour outgoingBehaviour) {

        this.activity = activity;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.outgoingTransitions = new ArrayList<Transition>();
        this.incomingTransitions = new ArrayList<Transition>();
    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoingBehaviour;
    }

    @Override
    public void setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour) {

        this.outgoingBehaviour = outgoingBehaviour;
    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {

        return incomingBehaviour;
    }

    @Override
    public void setIncomingBehaviour(IncomingBehaviour incomingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
    }

    /**
     * Instantiates a new node impl.
     * 
     * @param activity
     *            the activity
     */
    public NodeImpl(Activity activity) {

        this(activity, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
    }

    @Override
    public Activity getActivity() {

        return activity;
    }

    @Override
    public void setActivity(Activity activity) {

        this.activity = activity;
    }

    @Override
    public void transitionTo(Node node) {

        Condition c = new ConditionImpl();
        createTransitionWithCondition(node, c);
    }

    /**
     * Creates the transition with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     */
    private void createTransitionWithCondition(Node node, Condition c) {

        Transition t = new TransitionImpl(this, node, c);
        this.outgoingTransitions.add(t);
        List<Transition> nextIncoming = node.getIncomingTransitions();
        nextIncoming.add(t);
    }

    @Override
    public UUID getID() {

        return this.id;
    }

    /**
     * Sets the transitions.
     * 
     * @param transitions
     *            the new transitions
     */
    public void setTransitions(List<Transition> transitions) {

        this.outgoingTransitions = transitions;
    }

    @Override
    public List<Transition> getOutgoingTransitions() {

        return outgoingTransitions;
    }

    @Override
    public List<Transition> getIncomingTransitions() {

        return incomingTransitions;
    }

    @Override
    public void transitionToWithCondition(Node node, Condition c) {

        createTransitionWithCondition(node, c);

    }

}
