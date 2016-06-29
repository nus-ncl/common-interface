package sg.ncl.service.team.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.dtos.TeamMemberInfo;

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

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);

        assertThat(entity.getId(), is(id));
    }

    @Test
    public void testGetName() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getName(), is(nullValue()));
    }

    @Test
    public void testSetName() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);

        assertThat(entity.getName(), is(name));
    }

    @Test
    public void testGetDescription() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getDescription(), is(nullValue()));
    }

    @Test
    public void testSetDescription() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);

        assertThat(entity.getDescription(), is(description));
    }

    @Test
    public void testGetWebsite() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getWebsite(), is(nullValue()));
    }

    @Test
    public void testSetWebsite() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String website = RandomStringUtils.randomAlphanumeric(20);
        entity.setWebsite(website);

        assertThat(entity.getWebsite(), is(website));
    }

    @Test
    public void testGetOrganisationType() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getOrganisationType(), is(nullValue()));
    }

    @Test
    public void testSetOrganisationType() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final String organisationType = RandomStringUtils.randomAlphanumeric(20);
        entity.setOrganisationType(organisationType);

        assertThat(entity.getOrganisationType(), is(organisationType));
    }

    @Test
    public void testGetVisibility() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getVisibility(), is(TeamVisibility.PUBLIC));
    }

    @Test
    public void testSetVisibility() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setVisibility(TeamVisibility.PRIVATE);

        assertThat(entity.getVisibility(), is(TeamVisibility.PRIVATE));
    }

    @Test
    public void testGetPrivacy() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getPrivacy(), is(TeamPrivacy.OPEN));
    }

    @Test
    public void testSetPrivacy() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setPrivacy(TeamPrivacy.INVITE);

        assertThat(entity.getPrivacy(), is(TeamPrivacy.INVITE));
    }

    @Test
    public void testGetStatus() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getStatus(), is(TeamStatus.PENDING));
    }

    @Test
    public void testSetStatus() throws Exception {
        final TeamEntity entity = new TeamEntity();
        entity.setStatus(TeamStatus.APPROVED);

        assertThat(entity.getStatus(), is(TeamStatus.APPROVED));
    }

    @Test
    public void testGetApplicationDate() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getApplicationDate(), is(nullValue()));
    }

    @Test
    public void testSetApplicationDate() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();
        entity.setApplicationDate(zonedDateTime);

        assertThat(entity.getApplicationDate(), is(zonedDateTime));
    }

    @Test
    public void testGetProcessedDate() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getProcessedDate(), is(nullValue()));
    }

    @Test
    public void testSetProcessedDate() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();
        entity.setProcessedDate(zonedDateTime);

        assertThat(entity.getProcessedDate(), is(zonedDateTime));
    }

    @Test
    public void testGetMembers() throws Exception {
        final TeamEntity entity = new TeamEntity();

        assertThat(entity.getMembers(), is(new ArrayList<TeamMemberEntity>()));
    }

    @Test
    public void testAddMember() throws Exception {
        final TeamEntity entity = new TeamEntity();
        final TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo();
        entity.addMember(teamMemberInfo);

        assertThat(entity.getMembers().get(0).getUserId(), is(teamMemberInfo.getUserId()));
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
