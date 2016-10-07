package sg.ncl.service.data.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.data.data.jpa.DatasetDownloadEntity;
import sg.ncl.service.data.data.jpa.DatasetEntity;
import sg.ncl.service.data.data.jpa.DatasetResourceEntity;
import sg.ncl.service.data.domain.DatasetAccessibility;
import sg.ncl.service.data.domain.DatasetCategory;
import sg.ncl.service.data.domain.DatasetResourceType;
import sg.ncl.service.data.domain.DatasetStatus;
import sg.ncl.service.data.domain.DatasetVisibility;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class TestUtil {
    public static DatasetEntity getDatasetEntity() {
        return getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE, 1L, 0, DatasetCategory.DNS);
    }

    public static DatasetEntity getDatasetEntityWithResources() {
        DatasetEntity entity = getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE, 1L, 0, DatasetCategory.DNS);

        DatasetResourceEntity resourceEntity = getDatasetResourceEntity();
        List<DatasetResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        entity.setResources(resources);

        return entity;
    }

    public static DatasetEntity getDatasetEntityWithApprovedUsers() {
        DatasetEntity entity = getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE, 1L, 0, DatasetCategory.DNS);

        String user = "bob007";
        entity.addApprovedUser(user);

        return entity;
    }

    public static DatasetEntity getDatasetEntityWithDownloadHistory() {
        DatasetEntity entity = getDatasetEntity("dataset", "description", "ncl001", DatasetAccessibility.OPEN,
                DatasetVisibility.PUBLIC, DatasetStatus.COMPLETE, 1L, 0, DatasetCategory.DNS);

        DatasetDownloadEntity downloadEntity = getDatasetDownloadEntity();
        List<DatasetDownloadEntity> downloadHistory = new ArrayList<>();
        downloadHistory.add(downloadEntity);
        entity.setDownloadHistory(downloadHistory);

        return entity;
    }

    public static DatasetEntity getDatasetEntity(final String name,
                                                 final String description,
                                                 final String ownerId,
                                                 final DatasetAccessibility accessibility,
                                                 final DatasetVisibility visibility,
                                                 final DatasetStatus status,
                                                 final Long size,
                                                 final int downloadTimes,
                                                 final DatasetCategory category)
    {
        final DatasetEntity entity = new DatasetEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setOwnerId(ownerId);
        entity.setAccessibility(accessibility);
        entity.setVisibility(visibility);
        entity.setStatus(status);
        entity.setSize(size);
        entity.setDownloadTimes(downloadTimes);
        entity.setCategory(category);

        return entity;
    }

    public static DatasetResourceEntity getDatasetResourceEntity() {
        DatasetResourceEntity entity = new DatasetResourceEntity();
        entity.setLink("http://" + RandomStringUtils.randomAlphanumeric(20));
        entity.setType(DatasetResourceType.EXTERNAL);

        return entity;
    }

    public static DatasetDownloadEntity getDatasetDownloadEntity() {
        DatasetDownloadEntity entity = new DatasetDownloadEntity();
        entity.setUserId(RandomStringUtils.randomAlphanumeric(20));
        entity.setDate(ZonedDateTime.now());
        entity.setSuccess(true);

        return entity;
    }
}
