package org.jodaengine.rest.demo;

import java.io.File;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotActivatedException;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.node.factory.ControlFlowFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.CreationPatternBuilder;
import org.jodaengine.resource.allocation.CreationPatternBuilderImpl;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class DemoDataForWebservice {

    private static final Logger logger = LoggerFactory.getLogger(DemoDataForWebservice.class);

    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms";
    private static IdentityBuilder identityBuilder;
    private static AbstractRole role;
    public final static int NUMBER_OF_PROCESSINSTANCES = 10;
    private static boolean invoked = false;

    /**
     * Hidden constructor.
     */
    private DemoDataForWebservice() {

    }

    /**
     * Resets invoked, to be honest mostly for testing purposed after each method.
     */
    public synchronized static void resetInvoked() {

        invoked = false;
    }

    /**
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return identityBuilder;
    }

    /**
     * Generate example Participants.
     * 
     * @throws ResourceNotAvailableException
     *             no such resource
     */
    public static synchronized void generate()
    throws ResourceNotAvailableException {

        if (!invoked) {
            invoked = true;
            generateDemoParticipants();
            try {

                generateDemoWorklistItems();
            
            } catch (IllegalStarteventException e) {

                logger.error("An Exception occurred: " + e.getMessage(), e);

            } catch (ProcessArtifactNotFoundException e) {

                logger.error("An Exception occurred: " + e.getMessage(), e);

            } catch (DefinitionNotFoundException e) {

                logger.error("An Exception occurred: " + e.getMessage(), e);

            } catch (DefinitionNotActivatedException e) {
                
                logger.error("An Exception occurred: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Generate demo participants.
     * 
     * @throws ResourceNotAvailableException
     *             no such resource
     */
    private static void generateDemoParticipants()
    throws ResourceNotAvailableException {

        role = getBuilder().createRole("BPT");
        AbstractParticipant p1 = getBuilder().createParticipant("Thorben-demo");
        AbstractParticipant p2 = getBuilder().createParticipant("Tobi P.-demo");
        AbstractParticipant p3 = getBuilder().createParticipant("Tobi M.-demo");
        AbstractParticipant p4 = getBuilder().createParticipant("Gerardo-demo");
        AbstractParticipant p5 = getBuilder().createParticipant("Jan-demo");
        AbstractParticipant p6 = getBuilder().createParticipant("Jannik-demo");
        getBuilder().participantBelongsToRole(p1.getID(), role.getID())
        .participantBelongsToRole(p2.getID(), role.getID()).participantBelongsToRole(p3.getID(), role.getID())
        .participantBelongsToRole(p4.getID(), role.getID()).participantBelongsToRole(p5.getID(), role.getID())
        .participantBelongsToRole(p6.getID(), role.getID());
    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws IllegalStarteventException
     *             the registered start event was missing or not legally defined
     * @throws ProcessArtifactNotFoundException
     * @throws DefinitionNotFoundException
     * @throws DefinitionNotActivatedException
     */
    private static void generateDemoWorklistItems()
    throws IllegalStarteventException, ProcessArtifactNotFoundException, DefinitionNotFoundException,
    DefinitionNotActivatedException {

        BpmnProcessDefinitionBuilder processBuilder = BpmnProcessDefinitionBuilder.newBuilder();

        Node startNode, node1, node2, node3, endNode;

        startNode = BpmnNodeFactory.createBpmnStartEventNode(processBuilder);

        // Creating the Webform for the task
        RepositoryService repositoryService = ServiceFactory.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.getDeploymentBuilder();
        deploymentBuilder.addFileArtifact("form1", new File(PATH_TO_WEBFORMS + "/claimPoints.html"));

        // Create the task
        // AllocationStrategies strategies = new AllocationStrategiesImpl(new ConcreteResourcePattern(), new
        // SimplePullPattern(),
        // null, null);
        // Task task = new TaskImpl("do something", "Really do something we got a demo coming up guys!", form,
        // strategies,
        // role);
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();
        builder.setItemDescription("Really do something we got a demo coming up guys!").setItemSubject("do something")
        .setItemFormID("form1").addResourceAssignedToItem(role);
        CreationPattern pattern = builder.buildCreationPattern(RoleBasedDistributionPattern.class);

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, pattern);

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, pattern);

        // Create the task
        node3 = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, pattern);

        endNode = BpmnNodeFactory.createBpmnEndEventNode(processBuilder);

        ControlFlowFactory.createControlFlowFromTo(processBuilder, startNode, node1);
        ControlFlowFactory.createControlFlowFromTo(processBuilder, node1, node2);
        ControlFlowFactory.createControlFlowFromTo(processBuilder, node2, node3);
        ControlFlowFactory.createControlFlowFromTo(processBuilder, node3, endNode);

        // Start Process
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(processBuilder);
        ProcessDefinition processDefinition = processBuilder.setName("Demoprocess")
        .setDescription("A simple demo process with three human tasks.").buildDefinition();

        // ProcessDefinitionID processID = ServiceFactory.getRepositoryService().getDeploymentBuilder()
        // .deployProcessDefinition(new RawProcessDefintionImporter(processDefinition));
        deploymentBuilder.addProcessDefinition(processDefinition);

        Deployment deployment = deploymentBuilder.buildDeployment();
        repositoryService.deployInNewScope(deployment);
        
        repositoryService.activateProcessDefinition(processDefinition.getID());

        // create more tasks
        for (int i = 0; i < NUMBER_OF_PROCESSINSTANCES; i++) {
            ServiceFactory.getNavigatorService().startProcessInstance(processDefinition.getID());
        }

    }
}
