package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.AbstractProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Class ProcessEventImpl. Have a look at {@link IncomingProcessEvent}.
 */
public abstract class AbstractIncomingProcessEvent extends AbstractProcessEvent implements IncomingProcessEvent {

    private EventCondition condition;

    protected TriggeringBehavior triggeringBehavior;

    /**
     * Instantiates a new {@link IncomingProcessEvent}.
     * 
     * @param config
     *            the config
     * @param condition
     *            the conditions
     */
    protected AbstractIncomingProcessEvent(AdapterConfiguration config,
                                           EventCondition condition,
                                           TriggeringBehavior triggeringBehavior) {

        super(config);
        this.condition = condition;
        this.triggeringBehavior = triggeringBehavior;
    }

    @Override
    public EventCondition getCondition() {

        if (this.condition == null) {
            this.condition = new TrueEventCondition();
        }
        return condition;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return getCondition().evaluate(adapterEvent);
    }

    @Override
    public void trigger() {

        triggeringBehavior.trigger(this);
    }

    /**
     * Sets the TriggeringBehaviour for this {@link IncomingProcessEvent}.
     * 
     * @param triggeringBehavior
     *            - the {@link TriggeringBehavior} that should be set
     */
    public void setTriggeringBehaviour(TriggeringBehavior triggeringBehavior) {

        this.triggeringBehavior = triggeringBehavior;
    }
}
