package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractPosition;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Worklist for a participant.
 */
public class ParticipantWorklist extends AbstractDefaultWorklist {
    
    private AbstractParticipant relatedParticipant;
    
    /**
     * Hidden constructor.
     */
    protected ParticipantWorklist() { }
    
    /**
     * Instantiates a new participant worklist.
     *
     * @param owner the related participant to the work list 
     */
    public ParticipantWorklist(@Nonnull AbstractParticipant owner) {
        
        this.relatedParticipant = owner;
    }

    @Override
    public List<AbstractWorklistItem> getWorklistItems() {
        
        // this is needed for deserialization to not break the circular dependency
        if (relatedParticipant == null) {
            return Collections.emptyList();
        }
        
        // Extracting the resources related to this owner
        List<AbstractResource<?>> resourcesInView = new ArrayList<AbstractResource<?>>();
        resourcesInView.addAll(relatedParticipant.getMyRolesImmutable());
        for (AbstractPosition position: relatedParticipant.getMyPositionsImmutable()) {
            resourcesInView.add(position);
            resourcesInView.add(position.belongstoOrganization());
        }

        // Creating the list of worklistItems from the owner and the related resources
        List<AbstractWorklistItem> resultWorklistItems = new ArrayList<AbstractWorklistItem>();
        resultWorklistItems.addAll(getLazyWorklistItems());
        for (AbstractResource<?> resourceInView : resourcesInView) {
            resultWorklistItems.addAll(resourceInView.getWorklist().getWorklistItems());
        }

        return resultWorklistItems;
    }

    @Override
    public void itemIsAllocatedBy(AbstractWorklistItem worklistItem, AbstractResource<?> claimingResource) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        if (claimingResource.equals(relatedParticipant)) {

            if (!getLazyWorklistItems().contains(worklistItemImpl)) {

                addWorklistItem(worklistItemImpl);
            }

            worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
        } else {

            getLazyWorklistItems().remove(worklistItemImpl);
            worklistItemImpl.getAssignedResources().remove(relatedParticipant);
        }
    }

    @Override
    public void addWorklistItem(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedParticipant);
    }

    @Override
    public void itemIsCompleted(AbstractWorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);

        getLazyWorklistItems().remove(worklistItemImpl);
    }

    @Override
    public void itemIsStarted(AbstractWorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

}