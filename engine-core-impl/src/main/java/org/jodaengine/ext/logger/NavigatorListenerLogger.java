package org.jodaengine.ext.logger;

import javax.annotation.Nonnull;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.AbstractNavigatorListener;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A logger implementation, which observes the state of a navigator thread.
 */
// CHECKSTYLE:OFF
//
// The NavigatorListenerLogger test uses a spy object (mock with a real instance) provided by Mockito.
// Unfortunately Mockito does not support spies on final classes.
//
@Extension("logger-navigator-listener")
public class NavigatorListenerLogger
//CHECKSTYLE:ON
extends AbstractNavigatorListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    protected void stateChanged(@Nonnull Navigator navigator,
                                NavigatorState navState) {
        log(navigator);
    }
    
    /**
     * Intern method to log a state change.
     * 
     * @param navigator the navigator
     */
    private void log(@Nonnull Navigator navigator) {
        logger.info("NavigationListener: {}", navigator);
    }
}
