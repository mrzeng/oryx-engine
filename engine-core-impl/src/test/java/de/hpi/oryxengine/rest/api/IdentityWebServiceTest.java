package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests the interaction with our identity web service.
 */
public class IdentityWebServiceTest extends AbstractJsonServerTest {
    
    private AbstractParticipant jannik = null;
    private AbstractParticipant tobi = null;
    private AbstractRole r1 = null;
    private AbstractRole r2 = null;
    
    private static final String PARTICIPANT_URL = "/identity/participants";
    private static final String ROLES_URL = "/identity/roles";
    
    /**
     * Creates 2 participants.
     */
    public void create2Participants() {
        jannik = ParticipantFactory.createJannik();
        tobi = ParticipantFactory.createTobi();
    }
    
    /**
     * Creates 2 roles.
     */
    public void create2Roles() {
        
        IdentityBuilder builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        r1 = builder.createRole("test1");
        r2 = builder.createRole("test2");
    }
    
    @Override
    protected Class<?> getResource() {

        return IdentityWebService.class;
    }
    
    /**
     * Test get participants. It should get all participants.
     *
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException 
     */
    @Test
    public void testGetParticipants() throws URISyntaxException, IOException {
        create2Participants();
               
        String json = makeGETRequestReturningJson(PARTICIPANT_URL);
        Assert.assertNotNull(json);
        
        // cannot be directly deserialized to a set, as it is a container type. See documentation of .readValue()
        AbstractParticipant[] participants = this.mapper.readValue(json, AbstractParticipant[].class);
        Set<AbstractParticipant> set = new HashSet<AbstractParticipant>(Arrays.asList(participants));
        
        Assert.assertNotNull(participants);
        Assert.assertEquals(participants.length, 2);
        Assert.assertTrue(set.contains(jannik));
        Assert.assertTrue(set.contains(tobi)); 
    }
    
    /**
     * Test get participants with no participants.
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testGetParticipantsWithNoParticipants() throws URISyntaxException {        
        String json = makeGETRequestReturningJson(PARTICIPANT_URL);
        
        // [] is an empty JSON
        Assert.assertEquals(json, "[]");
    }
    
    /**
     * Test getall the roles(happy path).
     *
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetRoles() throws URISyntaxException, IOException {
        create2Roles();
        
        String json = makeGETRequestReturningJson(ROLES_URL);        
                
        // Same hack as above
        AbstractRole[] roles = this.mapper.readValue(json, AbstractRole[].class);
        Set<AbstractRole> set = new HashSet<AbstractRole>(Arrays.asList(roles));
        
        Assert.assertNotNull(json);
        Assert.assertNotNull(roles);
        Assert.assertEquals(roles.length, 2);
        Assert.assertTrue(set.contains(r1));
        Assert.assertTrue(set.contains(r2));
    }
    
    /**
     * Test get roles when there actually are no roles.
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testGetRolesWithNoRoles() throws URISyntaxException {
        String json = makeGETRequestReturningJson(ROLES_URL);
        // [] is an empty JSON.
        Assert.assertEquals(json, "[]");
    }

}
