package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;

import java.time.ZonedDateTime;

/**
 * Created by Desmond
 */
public class TeamCommon {

    public static TeamEntity createTeam() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        return teamEntity;
    }
}
