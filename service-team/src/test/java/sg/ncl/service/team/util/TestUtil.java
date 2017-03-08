package sg.ncl.service.team.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.web.TeamMemberInfo;

import java.time.ZonedDateTime;

import static org.mockito.Matchers.anyString;

/**
 * Created by Desmond
 */
public class TestUtil {

    public static TeamEntity getTeamEntityWithId() {
        final TeamEntity entity = new TeamEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(10));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setWebsite("http://" + RandomStringUtils.randomAlphanumeric(20) + ".com");
        entity.setOrganisationType(RandomStringUtils.randomAlphanumeric(20));
        entity.setApplicationDate(ZonedDateTime.now());
        return entity;
    }

    public static TeamEntity getTeamEntity() {
        final TeamEntity entity = new TeamEntity();
//        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(10));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setWebsite("http://" + RandomStringUtils.randomAlphanumeric(20) + ".com");
        entity.setOrganisationType(RandomStringUtils.randomAlphanumeric(20));
        entity.setApplicationDate(ZonedDateTime.now());
        return entity;
    }

    public static TeamMemberInfo getTeamMemberInfo(MemberType memberType, MemberStatus... memberStatuses) {
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        final TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setId(id);
        teamMemberEntity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(memberType);
        teamMemberEntity.setNotes(RandomStringUtils.randomAlphanumeric(20));
        if (memberStatuses.length > 0) {
            teamMemberEntity.setMemberStatus(memberStatuses[0]);
        }
        return new TeamMemberInfo(teamMemberEntity);
    }
}
