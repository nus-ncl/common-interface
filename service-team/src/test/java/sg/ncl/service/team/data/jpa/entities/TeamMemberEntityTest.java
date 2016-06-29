package sg.ncl.service.team.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.team.domain.TeamMemberType;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Desmond.
 */
public class TeamMemberEntityTest {

    @Test
    public void testGetId() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);

        assertThat(entity.getId(), is(id));
    }

    @Test
    public void testGetTeam() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();

        assertThat(entity.getTeam(), is(nullValue()));
    }

    @Test
    public void testSetTeam() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        final TeamEntity teamEntity = new TeamEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setName(name);
        entity.setTeam(teamEntity);

        assertThat(entity.getTeam().getName(), is(name));
    }

    @Test
    public void testGetUserId() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();

        assertThat(entity.getUserId(), is(nullValue()));
    }

    @Test
    public void testSetUserId() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        entity.setUserId(userId);

        assertThat(entity.getUserId(), is(userId));
    }

    @Test
    public void testGetJoinedDate() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();

        assertThat(entity.getJoinedDate(), is(nullValue()));
    }

    @Test
    public void testSetJoinedDate() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        final ZonedDateTime joinedDate = ZonedDateTime.now();
        entity.setJoinedDate(joinedDate);

        assertThat(entity.getJoinedDate(), is(joinedDate));
    }

    @Test
    public void testSetMemberType() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        entity.setTeamMemberType(TeamMemberType.OWNER);
        assertThat(entity.getTeamMemberType(), is(TeamMemberType.OWNER));
    }

    @Test
    public void testGetMemberType() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        assertThat(entity.getTeamMemberType(), is(notNullValue()));
    }
}
