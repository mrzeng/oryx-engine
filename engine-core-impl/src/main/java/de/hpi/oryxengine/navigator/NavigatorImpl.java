package de.hpi.oryxengine.navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processDefinition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstance.ProcessInstanceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class NavigatorImpl. Our Implementation of the Navigator.
 */
public class NavigatorImpl implements Navigator {

    // map IDs to Definition
    /** The running instances. */
    private HashMap<String, ProcessInstanceImpl> runningInstances;

    /** The loaded definitions. */
    private HashMap<String, AbstractProcessDefinitionImpl> loadedDefinitions;

    /** The to navigate. The list including all process isntances which are currently executed. */
    private List<ProcessInstance> toNavigate;

    /** The execution threads. Yes our navigator is multithreaded. Pretty awesome. */
    private ArrayList<NavigationThread> executionThreads;

    /** The Constant NUMBER_OF_NAVIGATOR_THREADS. */
    private static final int NUMBER_OF_NAVIGATOR_THREADS = 10;

    /**
     * Instantiates a new navigator impl.
     */
    public NavigatorImpl() {

        // TODO Lazy initialized
        runningInstances = new HashMap<String, ProcessInstanceImpl>();
        loadedDefinitions = new HashMap<String, AbstractProcessDefinitionImpl>();
        toNavigate = new LinkedList<ProcessInstance>();
        toNavigate = Collections.synchronizedList(toNavigate);
        executionThreads = new ArrayList<NavigationThread>();
    }

    /**
     * Start. 
     * Starts the number of worker thread specified in the NUMBER_OF_NAVIGATOR_THREADS Constant and adds them to
     * the execution threads list.
     */
    public void start() {

        // "Gentlemen, start your engines"
        for (int i = 0; i < NUMBER_OF_NAVIGATOR_THREADS; i++) {
            NavigationThread thread = new NavigationThread("NT" + i, toNavigate);
            thread.start();
            executionThreads.add(thread);
        }
    }

    /**
     * Starts the a proccesinstance of a process with the given ID.
     *
     * @param processID the process id
     * @return the string
     * @see de.hpi.oryxengine.navigator.Navigator#startProcessInstance(java.lang.String)
     */
    // TODO: Implement this thing in general
    public String startProcessInstance(String processID) {

        if (!loadedDefinitions.containsKey(processID)) {
            // go crazy
            // TODO: handle this errorcase
        }

        // instantiate the processDefinition
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl(loadedDefinitions.get(processID), 0);
        runningInstances.put(processInstance.getID(), processInstance);

        // we need to do this, as after node execution (in Navigator#signal() the currentNodes-Datastructure is altered.
        // Its not cool to change the datastructure you iterate over.
        toNavigate.add(processInstance);

        // tell the initial nodes to execute their activities

        return "aProcessInstanceID";
    }

    // this method is for first testing only, as we do not have ProcessDefinitions yet
    /**
     * Start arbitrary instance.
     * 
     * @param id
     *            the id
     * @param instance
     *            the instance
     */
    public void startArbitraryInstance(String id, ProcessInstanceImpl instance) {

        this.runningInstances.put(id, instance);
        this.toNavigate.add(instance);
    }

    /**
     * Adds the process definition to the Map, mapping from IDs to the processDefinition.
     * 
     * @param processDefinition
     *            the process definition
     */
    public void addProcessDefinition(AbstractProcessDefinitionImpl processDefinition) {

        loadedDefinitions.put(processDefinition.getId(), processDefinition);
    }

    /**
     * Stop the execution of a processinstance.
     *
     * @param instanceID the instance id
     * @see de.hpi.oryxengine.navigator.Navigator#stopProcessInstance(java.lang.String)
     */
    public void stopProcessInstance(String instanceID) {

        // TODO do some more stuff if instance doesnt exist and in genereal
        // runningInstances.remove(instanceID);
        // remove from queue...
    }

    /**
     * Get the state of the currently running instance.
     *
     * @param instanceID the instance id
     * @return the current instance state
     * @see de.hpi.oryxengine.navigator.Navigator#getCurrentInstanceState(java.lang.String)
     */
    public String getCurrentInstanceState(String instanceID) {

        // TODO get the current instance ID
        return null;
    }

    /**
     * Stop the Navigator. So in fact you need to stop all the Navigationthreads.
     */
    public void stop() {

        for (NavigationThread executionThread : executionThreads) {
            executionThread.setShouldStop(true);
        }
    }

}