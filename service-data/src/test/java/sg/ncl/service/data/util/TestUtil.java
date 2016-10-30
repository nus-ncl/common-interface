package sg.ncl.service.data.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.DataAccessibility;
import sg.ncl.service.data.domain.DataVisibility;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class TestUtil {
    public static DataEntity getDataEntity() {
        return getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);
    }

    public static DataEntity getDataEntityWithResources() {
        DataEntity entity = getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);

        DataResourceEntity resourceEntity = getDataResourceEntity();
        List<DataResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        entity.setResources(resources);

        return entity;
    }

    public static DataEntity getDataEntityWithApprovedUsers() {
        DataEntity entity = getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);

        String user = "bob007";
        entity.addApprovedUser(user);

        return entity;
    }

    public static DataEntity getDataEntity(final String name,
                                           final String description,
                                           final String ownerId,
                                           final DataAccessibility accessibility,
                                           final DataVisibility visibility
                                           )
    {
        final DataEntity entity = new DataEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setContributorId(ownerId);
        entity.setAccessibility(accessibility);
        entity.setVisibility(visibility);
        entity.setReleasedDate(ZonedDateTime.now());

        return entity;
    }

    public static DataResourceEntity getDataResourceEntity() {
        DataResourceEntity entity = new DataResourceEntity();
        entity.setUri("http://" + RandomStringUtils.randomAlphanumeric(20));

        return entity;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
