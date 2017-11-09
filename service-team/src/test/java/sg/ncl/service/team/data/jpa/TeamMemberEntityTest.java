package sg.ncl.service.team.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.team.domain.MemberPrivilege;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.*;
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
    public void testGetTeamNull() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();

        assertThat(entity.getTeam(), is(nullValue()));
    }

    @Test
    public void testGetTeamAvailable() {
        final TeamMemberEntity entity = new TeamMemberEntity();
        final TeamEntity teamEntity = new TeamEntity();
        final String name = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setName(name);
        entity.setTeam(teamEntity);

        assertThat(entity.getTeam(), is(teamEntity));
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
        entity.setMemberType(MemberType.OWNER);
        assertThat(entity.getMemberType(), is(MemberType.OWNER));
    }

    @Test
    public void testGetMemberType() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        assertThat(entity.getMemberType(), is(notNullValue()));
    }

    @Test
    public void testSetMemberStatus() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        entity.setMemberStatus(MemberStatus.APPROVED);
        assertThat(entity.getMemberStatus(), is(MemberStatus.APPROVED));
    }

    @Test
    public void testGetMemberStatus() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        assertThat(entity.getMemberStatus(), is(notNullValue()));
    }

    @Test
    public void testSetMemberPrivilege() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        entity.setMemberPrivilege(MemberPrivilege.USER);
        assertThat(entity.getMemberPrivilege(), is(MemberPrivilege.USER));
    }

    @Test
    public void testGetMemberPrivilege() throws Exception {
        final TeamMemberEntity entity = new TeamMemberEntity();
        assertThat(entity.getMemberPrivilege(), is(notNullValue()));
    }
}
