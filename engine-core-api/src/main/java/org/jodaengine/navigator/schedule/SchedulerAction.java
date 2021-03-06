package org.jodaengine.navigator.schedule;

/**
 * The Enum SchedulerAction.
 * It has the different Scheduler action.
 * SUBMIT means that a process token (or more) are submitted for scheduling.
 * RETRIEVE means that a processtoken is retrieved and will now be worked on.
 */
public enum SchedulerAction {

    /** The SUBMIT. */
    SUBMIT,

    /** The RETRIEVE. */
    RETRIEVE
}
