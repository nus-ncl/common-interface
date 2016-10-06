package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.data.domain.Dataset;
import sg.ncl.service.data.domain.DatasetAccessibility;
import sg.ncl.service.data.domain.DatasetStatus;
import sg.ncl.service.data.domain.DatasetVisibility;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/6/2016.
 */
public class DatasetEntityTest {

    @Test
    public void testGetId() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DatasetEntity entity = new DatasetEntity();
        String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetName() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getName()).isNull();
    }

    @Test
    public void testSetName() {
        DatasetEntity entity = new DatasetEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);
        assertThat(entity.getName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() {
        DatasetEntity entity = new DatasetEntity();
        String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);
        assertThat(entity.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetOwnerId() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getOwnerId()).isNull();
    }

    @Test
    public void testSetOwnerId() {
        DatasetEntity entity = new DatasetEntity();
        String ownerId = RandomStringUtils.randomAlphanumeric(20);
        entity.setOwnerId(ownerId);
        assertThat(entity.getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    public void testGetVisibility() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getVisibility()).isEqualTo(DatasetVisibility.PUBLIC);
    }

    @Test
    public void testSetVisibility() {
        DatasetEntity entity = new DatasetEntity();
        entity.setVisibility(DatasetVisibility.PROTECTED);
        assertThat(entity.getVisibility()).isEqualTo(DatasetVisibility.PROTECTED);
    }

    @Test
    public void testGetAccessibility() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getAccessibility()).isEqualTo(DatasetAccessibility.OPEN);
    }

    @Test
    public void testSetAccessibility() {
        DatasetEntity entity = new DatasetEntity();
        entity.setAccessibility(DatasetAccessibility.RESTRICTED);
        assertThat(entity.getAccessibility()).isEqualTo(DatasetAccessibility.RESTRICTED);
    }

    @Test
    public void testGetStatus() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getStatus()).isEqualTo(DatasetStatus.COMPLETE);
    }

    @Test
    public void testSetStatus() {
        DatasetEntity entity = new DatasetEntity();
        entity.setStatus(DatasetStatus.IN_PROGRESS);
        assertThat(entity.getStatus()).isEqualTo(DatasetStatus.IN_PROGRESS);
    }

    @Test
    public void testGetResources() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getResources().size()).isEqualTo(0);
    }

    @Test
    public void testSetResources() {
        DatasetEntity datasetEntity = new DatasetEntity();
        DatasetResourceEntity resourceEntity = new DatasetResourceEntity();
        List<DatasetResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        datasetEntity.setResources(resources);

        List<DatasetResourceEntity> retResources = datasetEntity.getResources();
        assertThat(retResources.size()).isEqualTo(1);
        assertThat(retResources.get(0)).isEqualTo(resourceEntity);
    }

    @Test
    public void testGetApprovedUsers() {
        DatasetEntity entity = new DatasetEntity();
        assertThat(entity.getApprovedUsers().size()).isEqualTo(0);
    }

    @Test
    public void testAddApprovedUsers() {
        DatasetEntity datasetEntity = new DatasetEntity();
        String user = RandomStringUtils.randomAlphanumeric(20);
        datasetEntity.addApprovedUser(user);

        List<String> approvedUsers = datasetEntity.getApprovedUsers();
        assertThat(approvedUsers.size()).isEqualTo(1);
        assertThat(approvedUsers.get(0)).isEqualTo(user);
    }

    @Test
    public void testDeleteApprovedUsers() {
        DatasetEntity datasetEntity = new DatasetEntity();
        String user = RandomStringUtils.randomAlphanumeric(20);
        datasetEntity.addApprovedUser(user);

        List<String> approvedUsers = datasetEntity.getApprovedUsers();
        assertThat(approvedUsers.size()).isEqualTo(1);
        assertThat(approvedUsers.get(0)).isEqualTo(user);

        datasetEntity.removeApprovedUser(user);
        assertThat(approvedUsers.size()).isEqualTo(0);
    }

    @Test
    public void testEquals() {
        DatasetEntity one = new DatasetEntity();
        DatasetEntity two = new DatasetEntity();

        assertThat(one).isEqualTo(two);

        String id = RandomStringUtils.randomAlphanumeric(20);
        one.setId(id);

        assertThat(one).isNotEqualTo(two);

        two.setId(id);

        assertThat(one).isEqualTo(two);
    }

    @Test
    public void testHashCode() {
        final DatasetEntity entity1 = new DatasetEntity();
        final DatasetEntity entity2 = new DatasetEntity();
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());

        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());

        entity2.setId(id1);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());

        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testToString() {
        final DatasetEntity entity = new DatasetEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setOwnerId(RandomStringUtils.randomAlphanumeric(20));
        entity.setVisibility(DatasetVisibility.PUBLIC);
        entity.setAccessibility(DatasetAccessibility.RESTRICTED);
        entity.setStatus(DatasetStatus.COMPLETE);
        entity.addApprovedUser(RandomStringUtils.randomAlphanumeric(20));

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId());
        assertThat(toString).contains(entity.getName());
        assertThat(toString).contains(entity.getDescription());
        assertThat(toString).contains(entity.getOwnerId());
        assertThat(toString).contains(entity.getVisibility().toString());
        assertThat(toString).contains(entity.getAccessibility().toString());
        assertThat(toString).contains(entity.getStatus().toString());
        assertThat(toString).contains(entity.getApprovedUsers().get(0));

    }
}
