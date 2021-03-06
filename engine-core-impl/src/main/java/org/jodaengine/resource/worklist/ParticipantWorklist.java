package org.jodaengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractPosition;
import org.jodaengine.resource.AbstractResource;

/**
 * Worklist for a participant.
 */
public class ParticipantWorklist extends AbstractDefaultWorklist {

    private AbstractParticipant relatedParticipant;

    /**
     * Hidden constructor.
     */
    protected ParticipantWorklist() {

    }

    /**
     * Instantiates a new participant worklist.
     * 
     * @param owner
     *            the related participant to the work list
     */
    public ParticipantWorklist(@Nonnull AbstractParticipant owner) {

        this.relatedParticipant = owner;
    }

    /**
     * Gets the resources in view. These are all roles, positions, etc. this participant belongs to.
     * 
     * @return the resources in view
     */
    private List<AbstractResource<?>> getResourcesInView() {

        List<AbstractResource<?>> resourcesInView = new ArrayList<AbstractResource<?>>();
        resourcesInView.addAll(relatedParticipant.getMyRolesImmutable());
        for (AbstractPosition position : relatedParticipant.getMyPositionsImmutable()) {
            resourcesInView.add(position);
            resourcesInView.add(position.belongstoOrganization());
        }

        return resourcesInView;
    }

    @Override
    public List<AbstractWorklistItem> getWorklistItems() {

        // this is needed for deserialization to not break the circular dependency
        if (relatedParticipant == null) {
            return Collections.emptyList();
        }

        // Extracting the resources related to this owner
        List<AbstractResource<?>> resourcesInView = getResourcesInView();

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

        // if the person who claims the item is the owner of this worklist, set the item state to ALLOCATED and add it
        // to this worklist if not present already
        if (claimingResource.equals(relatedParticipant)) {

            worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
            if (!getLazyWorklistItems().contains(worklistItemImpl)) {
                getLazyWorklistItems().add(worklistItemImpl);
            }

        } else {
            // if an item has been offered before, it now gets deleted here from the list, because somebody else claimed
            // it
            removeWorklistItem(worklistItem);
        }
    }

    @Override
    public synchronized void addWorklistItem(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedParticipant);
    }

    @Override
    public void removeWorklistItem(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedParticipant);

    }

    @Override
    public void itemIsCompleted(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().remove(worklistItem);
        
        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);
    }

    @Override
    public void itemIsStarted(AbstractWorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

}
