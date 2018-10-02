package sg.ncl.service.analytics.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.analytics.domain.ProjectUsage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.service.analytics.util.TestUtil.getProjectDetailsEntity;
import static sg.ncl.service.analytics.util.TestUtil.getProjectUsageEntity;

public class ProjectDetailsEntityTest {

    @Test
    public void testGetId() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetOrganisationType() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getOrganisationType()).isNull();
    }

    @Test
    public void testSetOrganisationType() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        String type = RandomStringUtils.randomNumeric(10);
        entity.setOrganisationType(type);
        assertThat(entity.getOrganisationType()).isEqualTo(type);
    }

    @Test
    public void testGetOrganisationName() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getOrganisationName()).isNull();
    }

    @Test
    public void testSetOrganisationName() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        String name = RandomStringUtils.randomNumeric(10);
        entity.setOrganisationName(name);
        assertThat(entity.getOrganisationName()).isEqualTo(name);
    }

    @Test
    public void testGetProjectName() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getProjectName()).isNull();
    }

    @Test
    public void testSetProjectName() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        String project = RandomStringUtils.randomNumeric(10);
        entity.setProjectName(project);
        assertThat(entity.getProjectName()).isEqualTo(project);
    }

    @Test
    public void testGetOwner() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getOwner()).isNull();
    }

    @Test
    public void testSetOwner() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        String owner = RandomStringUtils.randomNumeric(10);
        entity.setOwner(owner);
        assertThat(entity.getOwner()).isEqualTo(owner);
    }

    @Test
    public void testGetDateCreated() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getDateCreated()).isNull();
    }

    @Test
    public void testSetDateCreated() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        ZonedDateTime date = ZonedDateTime.now();
        entity.setDateCreated(date);
        assertThat(entity.getDateCreated()).isEqualTo(date);
    }

    @Test
    public void testIsEducation() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.isEducation()).isFalse();
    }

    @Test
    public void testSetIsEducation() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        entity.setEducation(true);
        assertThat(entity.isEducation()).isTrue();
    }

    @Test
    public void testIsServiceTool() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.isServiceTool()).isFalse();
    }

    @Test
    public void testSetIsServiceTool() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        entity.setServiceTool(true);
        assertThat(entity.isServiceTool()).isTrue();
    }

    @Test
    public void testGetSupportedBy() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getSupportedBy()).isNull();
    }

    @Test
    public void testSetSupportedBy() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        String support = RandomStringUtils.randomNumeric(10);
        entity.setSupportedBy(support);
        assertThat(entity.getSupportedBy()).isEqualTo(support);
    }

    @Test
    public void testGetProjectUsages() {
        ProjectDetailsEntity entity = new ProjectDetailsEntity();
        assertThat(entity.getProjectUsages().size()).isEqualTo(0);
    }

    @Test
    public void testSetProjectUsages() {
        ProjectDetailsEntity detailsEntity = new ProjectDetailsEntity();
        ProjectUsageEntity usageEntity = new ProjectUsageEntity();
        List<ProjectUsageEntity> usages = new ArrayList<>();
        usages.add(usageEntity);
        detailsEntity.setProjectUsages(usages);

        List<? extends ProjectUsage> retUsages = detailsEntity.getProjectUsages();
        assertThat(retUsages.size()).isEqualTo(1);
        assertThat(retUsages.get(0)).isEqualTo(usageEntity);
    }

    @Test
    public void testUpdateProjectUsage() {
        ProjectDetailsEntity detailsEntity = getProjectDetailsEntity();
        ProjectUsageEntity usageEntity = getProjectUsageEntity();
        ProjectUsageIdentity identity = usageEntity.getId();
        Integer monthlyUsage = usageEntity.getMonthlyUsage();
        detailsEntity.addProjectUsage(usageEntity);

        usageEntity.setMonthlyUsage(monthlyUsage + 10000);
        ProjectUsage savedEntity = detailsEntity.updateProjectUsage(usageEntity);
        assertThat(savedEntity.getId()).isEqualTo(identity);
        assertThat(savedEntity.getMonthlyUsage()).isEqualTo(monthlyUsage + 10000);
    }

    @Test
    public void testUpdateProjectUsageNotFound() {
        ProjectDetailsEntity detailsEntity = getProjectDetailsEntity();
        ProjectUsageEntity projectUsageEntity = getProjectUsageEntity();
        ProjectUsageEntity updatedUsageEntity = getProjectUsageEntity();
        detailsEntity.addProjectUsage(projectUsageEntity);
        ProjectUsage savedEntity = detailsEntity.updateProjectUsage(updatedUsageEntity);
        assertThat(savedEntity).isNull();
    }
}
