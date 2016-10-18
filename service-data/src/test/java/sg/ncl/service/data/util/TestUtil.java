package sg.ncl.service.data.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.data.jpa.DataStatisticsEntity;
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

    public static DataEntity getDataEntityWithStatistics() {
        DataEntity entity = getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);

        DataStatisticsEntity statisticsEntity = getDataStatisticsEntity();
        List<DataStatisticsEntity> statistics = new ArrayList<>();
        statistics.add(statisticsEntity);
        entity.setStatistics(statistics);

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

        return entity;
    }

    public static DataResourceEntity getDataResourceEntity() {
        DataResourceEntity entity = new DataResourceEntity();
        entity.setUri("http://" + RandomStringUtils.randomAlphanumeric(20));

        return entity;
    }

    public static DataStatisticsEntity getDataStatisticsEntity() {
        DataStatisticsEntity entity = new DataStatisticsEntity();
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setDate(ZonedDateTime.now());

        return entity;
    }
}
