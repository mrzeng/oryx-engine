package org.jodaengine.ext.debugging.listener;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.shared.DebuggerInstanceAttribute;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process execution
 * using the {@link Token} takes place and can handle {@link Breakpoint}s, if those are defined.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-22
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerTokenListener extends AbstractTokenListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DebuggerServiceImpl debugger;
    private Navigator navigator;
    
    /**
     * Default constructor. Creates a {@link DebuggerTokenListener} for a {@link DebuggerService}.
     * 
     * @param debugger the debugger service, this listener belongs to
     * @param navigator the navigator service, which schedules the instances
     */
    public DebuggerTokenListener(@Nonnull DebuggerServiceImpl debugger,
                                 @Nonnull Navigator navigator) {
        this.debugger = debugger;
        this.navigator = navigator;
    }
    
    @Override
    public void stateChanged(ActivityLifecycleChangeEvent event) {
        
        logger.debug("DebuggerTokenListener#stateChanged {} for {}", event, this.debugger);
        
        //
        // get the token
        //
        Token token = event.getProcessToken();
        DebuggerInstanceAttribute attribute = DebuggerInstanceAttribute.getAttribute(token);
        
        //
        // keep chosen path for history purposes
        //
        if (ActivityState.INIT.equals(token.getCurrentActivityState())) {
            if (token.getLastTakenControlFlow() != null) {
                attribute.addPreviousPath(token.getLastTakenControlFlow(), token);
            }
        }
        
        //
        // consider the active command (it is removed!)
        // -> break or leave, if required (step over vs. resume)
        //
        DebuggerCommand command = attribute.getCommand(token);
        if (command != null) {
            switch (command) {
                case CONTINUE:
                    //
                    // there needs nothing to be done here
                    //
                    break;
                case RESUME:
                    //
                    // any following breakpoints are ignored
                    // -> leave
                    //
//                    attribute.setCommand(token, DebuggerCommand.RESUME);
                    return;
                case STEP_OVER:
                    //
                    // break, no matter if there is a breakpoint
                    // -> break and 
                    //
                    interruptInstance(token, attribute, null);
                    return;
                case TERMINATE:
                    //
                    // we already canceled the instance
                    //
//                    attribute.setCommand(token, DebuggerCommand.TERMINATE);
                    return;
                default:
                    //
                    // unknown command
                    //
            }
        }
        
        //
        // get the breakpoints
        //
        Collection<Breakpoint> breakpoints = this.debugger.getBreakpoints(token.getInstance());
        
        //
        // case: no
        //
        if (breakpoints.isEmpty()) {
            return;
        }
        
        //
        // case: yes - does any breakpoint match?
        //
        for (Breakpoint breakpoint: breakpoints) {
            
            if (breakpoint.matches(token)) {
                logger.debug("Breakpoint {} matches {}", breakpoint, token);
                
                //
                // break
                //
                interruptInstance(token, attribute, breakpoint);
                
                //
                // ignore any subsequent breakpoints at this point and go on with the process instance
                //
                return;
            } else {
                logger.debug("Breakpoint {} did not match {}", breakpoint, token);
            }
        }
    }
    
    /**
     * This method interrupts a process instance ({@link Token}), either because a {@link Breakpoint} matched
     * or because the {@link DebuggerCommand} indicated a break.
     * 
     * @param token the token to interrupt
     * @param debuggerState the token's {@link DebuggerInstanceAttribute}
     * @param breakpoint the breakpoint, which matched, if any
     */
    public void interruptInstance(@Nonnull Token token,
                                  @Nonnull DebuggerInstanceAttribute debuggerState,
                                  @Nullable Breakpoint breakpoint) {
        
        //
        // inform the DebuggerService
        //
        Interrupter signal = this.debugger.breakTriggered(token, breakpoint, this);
        
        //
        // interrupt the token
        //
        try {
            logger.info("Interrupting token {}", token);
            DebuggerCommand command = signal.interruptInstance();
            
            logger.info("Token {} released with command {}", token, command);
            debuggerState.setCommand(token, command);
            
            //
            // consider the command and go on
            //
            assert command != null;
            
            switch (command) {
                case CONTINUE:
                    //
                    // there needs nothing to be done here
                    // -> the next breakpoint will automatically match
                    //
                    break;
                case RESUME:
                    //
                    // there needs nothing to be done here
                    // -> before the next breakpoint matches it is evaluated,
                    //    whether the process was resumed
                    //
                    break;
                case STEP_OVER:
                    //
                    // there needs nothing to be done here
                    // -> before the next breakpoint matches it is evaluated,
                    //    whether the process was stepped over
                    //
                    break;
                case TERMINATE:
                    //
                    // we cancel the process execution
                    //
                    this.navigator.cancelProcessInstance(token.getInstance());
                    break;
                default:
                    logger.warn("Released token {} with unknown command {}.", token, command);
            }
        } catch (InterruptedException ie) {
            //
            // Some other thread interrupted this one unexpectedly.
            // This means that the Debugger will no longer wait for this token (breakpoint).
            //
            logger.warn("Unexpected thread interruption. Token " + token.toString() + " will continue.", ie);
            
            //
            // we inform the Debugger, so it may unregister the breakpoint
            //
            this.debugger.unexspectedInterruption(signal);
        }
    }
}
