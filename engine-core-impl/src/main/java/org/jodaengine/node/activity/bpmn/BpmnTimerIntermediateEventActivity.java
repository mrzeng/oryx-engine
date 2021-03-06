package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.EventSubscriptionManagement;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.AbstractIncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.TimerIntermediateProcessEvent;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * The {@link BpmnTimerIntermediateEventActivity IntermediateTimer} is used to wait a specific amount of time before
 * execution is continued.
 */
public class BpmnTimerIntermediateEventActivity extends AbstractCancelableActivity implements
BpmnEventBasedGatewayEvent {

    private long time;

    private static final String PROCESS_EVENT_PREFIX = "PROCESS_EVENT-";

    /**
     * Instantiates a new intermediate timer with a given time.
     * 
     * @param time
     *            - the time (in ms) to wait for
     */
    public BpmnTimerIntermediateEventActivity(long time) {

        this.time = time;
    }

    @Override
    protected void executeIntern(@Nonnull AbstractToken token) {

        EventSubscriptionManagement eventManager = token.getEventManagerService();

        IncomingIntermediateProcessEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.subscribeToIncomingIntermediateEvent(processEvent);

        // the name should be unique, as the token can only work on one activity at a time.
        token.setInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, token), processEvent);

        token.suspend();
    }

    @Override
    public void resume(Token token, Object resumeObject) {

        super.resume(token, resumeObject);

        // Doing nothing because the fact the this method already means that the TimerEvent occurred
    }

    @Override
    public void cancelIntern(AbstractToken executingToken) {

        IncomingIntermediateProcessEvent intermediateEvent = (IncomingIntermediateProcessEvent) executingToken
        .getInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, executingToken));

        EventSubscriptionManagement eventManager = executingToken.getEventManagerService();
        eventManager.unsubscribeFromIncomingIntermediateEvent(intermediateEvent);
    }

    //
    // ==== Interface that represents that this event can also be used by other nodes like event-based Gateway ====
    //
    
    // TODO @Gerardo: Ok really... this has to change. Period.
    @Override
    public AbstractIncomingIntermediateProcessEvent createProcessIntermediateEvent(Token token) {

        return new TimerIntermediateProcessEvent(time, token);
    }
}
