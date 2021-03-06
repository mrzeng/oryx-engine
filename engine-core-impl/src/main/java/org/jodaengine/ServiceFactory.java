package org.jodaengine;

import javax.annotation.Nonnull;

import org.codehaus.jackson.map.ObjectMapper;
import org.jodaengine.bootstrap.JodaEngineAppContext;
import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.EventService;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.worklist.WorklistService;
import org.jodaengine.resource.worklist.WorklistServiceIntern;


/**
 * General service factory, which provides singleton instances for our system.
 */
public final class ServiceFactory {

    /**
     * Gets the {@link WorklistManager} instance.
     * 
     * @return the worklist manager instance
     */
    protected static @Nonnull WorklistManager getWorklistManagerInstance() {

        return (WorklistManager) JodaEngineAppContext.getBean("worklistService");
    }

    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the worklist service
     */
    public static @Nonnull WorklistService getWorklistService() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link WorklistServiceIntern} Interface for operating on work lists.
     * 
     * @return the worklist queue
     */
    public static @Nonnull WorklistServiceIntern getInteralWorklistService() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link IdentityService}.
     * 
     * @return the identity service
     */
    public static @Nonnull IdentityService getIdentityService() {

        return (IdentityService) JodaEngineAppContext.getBean("identityService");
    }

    /**
     * Gets the {@link Navigator}.
     * 
     * @return the navigator service
     */
    public static @Nonnull Navigator getNavigatorService() {

        return (Navigator) JodaEngineAppContext.getBean("navigatorService");
    }

    /**
     * Gets the {@link RepositoryService}.
     * 
     * @return the repository service
     */
    public static @Nonnull RepositoryService getRepositoryService() {

        return (RepositoryService) JodaEngineAppContext.getBean("repositoryService");
    }

    /**
     * Gets the {@link EventCorrelator} for the supplied navigator. As we do not necessarily have only one navigator,
     * we need a EventManager for each of them.
     * 
     * @return the event service
     */
    public static @Nonnull EventService getEventManagerService() {

        return (EventService) JodaEngineAppContext.getBean("eventManagerService");
    }
    
    /**
     * Gets the {@link ExtensionService}.
     * 
     * @return the extension service
     */
    public static ExtensionService getExtensionService() {

        return (ExtensionService) JodaEngineAppContext.getBean("extensionService");
    }
    
    /**
     * Provides the object mapper.
     * 
     * @return the object mapper
     */
    public static @Nonnull ObjectMapper getJsonMapper() {
        return (ObjectMapper) JodaEngineAppContext.getBean("jsonMapper");
    }

    /**
     * Hidden Constructor.
     */
    private ServiceFactory() { }
}
