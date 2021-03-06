package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;


/**
 * The Class EndActivity. Just the activity which gets executed on the endevent. So nothing is done, in the future maybe
 * more should be done.
 */
public class BpmnEndEventActivity extends AbstractActivity {

    @Override
    protected void executeIntern(@Nonnull AbstractToken token) {

        // as this token has finished, it is removed from the instance, because it is not needed anymore.
        token.getInstance().removeToken(token);

        if (!token.getInstance().hasAssignedTokens()) {
            // there are no tokens assigned to this instance any longer, so it has finished (as the parameter tokens was
            // the last one).
            token.getNavigator().signalEndedProcessInstance(token.getInstance());
        }
        // logger.info("Completed Process", token.getID());
        // TODO Add persistence for process context variables, if we have a method for persistence.

    }
}
