package sg.ncl.service.team.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Desmond Lim
 */
public class TeamEntityTest {

    @Test
    public void testGetId() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertNull(entity.getId());
    }

    @Test
    public void testSetId() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);

        assertEquals(entity.getId(), id);
    }

    @Test
    public void testGetName() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertNull(entity.getName());
    }

    @Test
    public void testSetName() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);

        assertEquals(entity.getName(), name);
    }

    @Test
    public void testGetDescription() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertNull(entity.getDescription());
    }

    @Test
    public void testSetDescription() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);

        assertEquals(entity.getDescription(), description);
    }

    @Test
    public void testGetVisibility() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertEquals(entity.getVisibility(), TeamVisibility.PUBLIC);
    }

    @Test
    public void testSetVisibility() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setVisibility(TeamVisibility.PRIVATE);

        assertEquals(entity.getVisibility(), TeamVisibility.PRIVATE);
    }

    @Test
    public void testGetPrivacy() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertEquals(entity.getPrivacy(), TeamPrivacy.OPEN);
    }

    @Test
    public void testSetPrivacy() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setPrivacy(TeamPrivacy.INVITE);

        assertEquals(entity.getPrivacy(), TeamPrivacy.INVITE);
    }

    @Test
    public void testGetStatus() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertEquals(entity.getStatus(), TeamStatus.PENDING);
    }

    @Test
    public void testSetStatus() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setStatus(TeamStatus.APPROVED);

        assertEquals(entity.getStatus(), TeamStatus.APPROVED);
    }

    @Test
    public void testGetApplicationDate() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertNull(entity.getApplicationDate());
    }

    @Test
    public void testSetApplicationDate() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();
        entity.setApplicationDate(zonedDateTime);

        assertEquals(entity.getApplicationDate(), zonedDateTime);
    }

    @Test
    public void testGetProcessedDate() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertNull(entity.getProcessedDate());
    }

    @Test
    public void testSetProcessedDate() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();
        entity.setProcessedDate(zonedDateTime);

        assertEquals(entity.getProcessedDate(), zonedDateTime);
    }

    @Test
    public void testGetMembers() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getMembers(), is(new ArrayList<TeamMemberEntity>()));
    }

    @Test
    public void testAddMember() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        entity.addMember(userId);

        assertEquals(entity.getMembers().get(0).getUserId(), userId);
    }

    @Test
    public void testEqual() throws Exception {
        final TeamEntity entity1 = Util.getTeamEntity();
        final TeamEntity entity2 = Util.getTeamEntity();

        assertThat(entity1, not(entity2));
    }

    @Test
    public void testHashCode() throws Exception {
        final TeamEntity entity1 = new TeamEntity();
        final TeamEntity entity2 = new TeamEntity();

        assertThat(entity1.hashCode(), is(equalTo(entity2.hashCode())));

        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);

        assertThat(entity1.hashCode(), is(not(equalTo(entity2.hashCode()))));

        entity2.setId(id1);

        assertThat(entity1.hashCode(), is(equalTo(entity2.hashCode())));

        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);

        assertThat(entity1.hashCode(), is(not(equalTo(entity2.hashCode()))));
    }

    @Test
    public void testToString() throws Exception {
        final TeamEntity entity = Util.getTeamEntity();

        final String toString = entity.toString();

        assertThat(toString, containsString(entity.getId()));
        assertThat(toString, containsString(entity.getName()));
        assertThat(toString, containsString(entity.getDescription()));
        assertThat(toString, containsString(TeamVisibility.PUBLIC.toString()));
        assertThat(toString, containsString(TeamStatus.PENDING.toString()));
    }
}
