package sg.ncl.service.image.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.image.data.jpa.ImageEntity;
import sg.ncl.service.image.domain.ImageVisibility;

/**
 * @author Christopher Zhong
 */
public class TestUtil {

    public static ImageEntity getImageEntity() {
        return getImageEntity(Long.parseLong(RandomStringUtils.randomNumeric(15)), RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), ImageVisibility.PRIVATE, RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20));
    }

    public static ImageEntity getImageEntity(final Long id, final String imageName, final String description, final ImageVisibility visibility, final String nodeId, final String teamId) {
        final ImageEntity entity = new ImageEntity();
        entity.setId(id);
        entity.setImageName(imageName);
        entity.setDescription(description);
        entity.setVisibility(visibility);
        entity.setNodeId(nodeId);
        entity.setTeamId(teamId);
        return entity;
    }

}
