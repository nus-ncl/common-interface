package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.data.domain.DataAccessibility;
import sg.ncl.service.data.domain.DataVisibility;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcszwang on 10/6/2016.
 */
public class DataEntityTest {

    @Test
    public void testGetId() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataEntity entity = new DataEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetName() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getName()).isNull();
    }

    @Test
    public void testSetName() {
        DataEntity entity = new DataEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);
        assertThat(entity.getName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() {
        DataEntity entity = new DataEntity();
        String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);
        assertThat(entity.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetContributorId() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getContributorId()).isNull();
    }

    @Test
    public void testSetOwnerId() {
        DataEntity entity = new DataEntity();
        String ownerId = RandomStringUtils.randomAlphanumeric(20);
        entity.setContributorId(ownerId);
        assertThat(entity.getContributorId()).isEqualTo(ownerId);
    }

    @Test
    public void testGetVisibility() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getVisibility()).isEqualTo(DataVisibility.PUBLIC);
    }

    @Test
    public void testSetVisibilityProtected() {
        DataEntity entity = new DataEntity();
        entity.setVisibility(DataVisibility.PROTECTED);
        assertThat(entity.getVisibility()).isEqualTo(DataVisibility.PROTECTED);
    }

    @Test
    public void testSetVisibilityPrivate() {
        DataEntity entity = new DataEntity();
        entity.setVisibility(DataVisibility.PRIVATE);
        assertThat(entity.getVisibility()).isEqualTo(DataVisibility.PRIVATE);
    }

    @Test
    public void testGetAccessibility() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getAccessibility()).isEqualTo(DataAccessibility.OPEN);
    }

    @Test
    public void testSetAccessibility() {
        DataEntity entity = new DataEntity();
        entity.setAccessibility(DataAccessibility.RESTRICTED);
        assertThat(entity.getAccessibility()).isEqualTo(DataAccessibility.RESTRICTED);
    }

    @Test
    public void testGetResources() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getResources().size()).isEqualTo(0);
    }

    @Test
    public void testSetResources() {
        DataEntity dataEntity = new DataEntity();
        DataResourceEntity resourceEntity = new DataResourceEntity();
        List<DataResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        dataEntity.setResources(resources);

        List<DataResourceEntity> retResources = dataEntity.getResources();
        assertThat(retResources.size()).isEqualTo(1);
        assertThat(retResources.get(0)).isEqualTo(resourceEntity);
    }

    @Test
    public void testGetApprovedUsers() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getApprovedUsers().size()).isEqualTo(0);
    }

    @Test
    public void testAddApprovedUsers() {
        DataEntity dataEntity = new DataEntity();
        String user = RandomStringUtils.randomAlphanumeric(20);
        dataEntity.addApprovedUser(user);

        List<String> approvedUsers = dataEntity.getApprovedUsers();
        assertThat(approvedUsers.size()).isEqualTo(1);
        assertThat(approvedUsers.get(0)).isEqualTo(user);
    }

    @Test
    public void testDeleteApprovedUsers() {
        DataEntity dataEntity = new DataEntity();
        String user = RandomStringUtils.randomAlphanumeric(20);
        dataEntity.addApprovedUser(user);

        List<String> approvedUsers = dataEntity.getApprovedUsers();
        assertThat(approvedUsers.size()).isEqualTo(1);
        assertThat(approvedUsers.get(0)).isEqualTo(user);

        dataEntity.removeApprovedUser(user);
        assertThat(approvedUsers.size()).isEqualTo(0);
    }

    @Test
    public void testGetDownloadHistory() {
        DataEntity entity = new DataEntity();
        assertThat(entity.getStatistics().size()).isEqualTo(0);
    }

    @Test
    public void testSetDownloadHistory() {
        DataEntity dataEntity = new DataEntity();
        DataStatisticsEntity statisticsEntity = new DataStatisticsEntity();
        List<DataStatisticsEntity> statistics = new ArrayList<>();
        statistics.add(statisticsEntity);
        dataEntity.setStatistics(statistics);

        List<DataStatisticsEntity> retStatistics = dataEntity.getStatistics();
        assertThat(retStatistics.size()).isEqualTo(1);
        assertThat(retStatistics.get(0)).isEqualTo(statisticsEntity);
    }

    @Test
    public void testEquals() {
        DataEntity one = new DataEntity();
        DataEntity two = new DataEntity();

        assertThat(one).isEqualTo(two);

        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        one.setId(id);

        assertThat(one).isNotEqualTo(two);

        two.setId(id);

        assertThat(one).isEqualTo(two);
    }

    @Test
    public void testHashCode() {
        final DataEntity entity1 = new DataEntity();
        final DataEntity entity2 = new DataEntity();
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());

        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());

        entity2.setId(id1);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());

        final Long id2 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity2.setId(id2);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testToString() {
        final DataEntity entity = new DataEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setContributorId(RandomStringUtils.randomAlphanumeric(20));
        entity.setVisibility(DataVisibility.PUBLIC);
        entity.setAccessibility(DataAccessibility.RESTRICTED);

        final String toString = entity.toString();

        assertThat(toString).contains(entity.getId().toString());
        assertThat(toString).contains(entity.getName());
        assertThat(toString).contains(entity.getDescription());
        assertThat(toString).contains(entity.getContributorId());
        assertThat(toString).contains(entity.getVisibility().toString());
        assertThat(toString).contains(entity.getAccessibility().toString());
    }
}
