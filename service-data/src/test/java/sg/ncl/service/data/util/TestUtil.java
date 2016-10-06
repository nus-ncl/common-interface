package sg.ncl.service.data.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.data.data.jpa.DatasetEntity;
import sg.ncl.service.data.data.jpa.DatasetResourceEntity;
import sg.ncl.service.data.domain.DatasetAccessibility;
import sg.ncl.service.data.domain.DatasetResourceType;
import sg.ncl.service.data.domain.DatasetStatus;
import sg.ncl.service.data.domain.DatasetVisibility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class TestUtil {
    public static DatasetEntity getDatasetEntity() {
        return getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE);
    }

    public static DatasetEntity getDatasetEntityWithResources() {
        DatasetEntity entity = getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE);

        DatasetResourceEntity resourceEntity = getDatasetResourceEntity();
        List<DatasetResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        entity.setResources(resources);

        return entity;
    }

    public static DatasetEntity getDatasetEntityWithApprovedUsers() {
        DatasetEntity entity = getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE);

        String user = "bob007";
        entity.addApprovedUser(user);

        return entity;
    }

    public static DatasetEntity getDatasetEntity(final String name,
                                                 final String description,
                                                 final String ownerId,
                                                 final DatasetAccessibility accessibility,
                                                 final DatasetVisibility visibility,
                                                 final DatasetStatus status)
    {
        final DatasetEntity entity = new DatasetEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setOwnerId(ownerId);
        entity.setAccessibility(accessibility);
        entity.setVisibility(visibility);
        entity.setStatus(status);

        return entity;
    }

    public static DatasetResourceEntity getDatasetResourceEntity() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        entity.setLink("http://" + RandomStringUtils.randomAlphanumeric(20));
        entity.setType(DatasetResourceType.EXTERNAL);

        return entity;
    }
}
