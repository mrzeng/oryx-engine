package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Class ProcessEventImpl.
 */
public class ProcessEventImpl implements ProcessEvent {
    
    /** The type. */
    private AdapterType type;
    
    /** The config. */
    private PullAdapterConfiguration config;
    
    /** The conditions. */
    private List<EventCondition> conditions;

    /**
     * Instantiates a new process event impl.
     *
     * @param type the type
     * @param config the config
     * @param conditions the conditions
     */
    protected ProcessEventImpl(AdapterType type,
                               PullAdapterConfiguration config,
                               List<EventCondition> conditions) {
        this.type = type;
        this.config = config;
        this.conditions = conditions;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AdapterType getAdapterType() {

        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PullAdapterConfiguration getAdapterConfiguration() {

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventCondition> getConditions() {

        return conditions;
    }

}
