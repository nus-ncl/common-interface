package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;

/**
 * Created by Desmond
 */
public class Util {

    public static TeamEntity getTeamEntity() {
        final TeamEntity entity = new TeamEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        return entity;
    }
}
