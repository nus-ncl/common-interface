package sg.ncl.service.analytics.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.analytics.data.jpa.ProjectDetailsEntity;
import sg.ncl.service.analytics.data.jpa.ProjectUsageEntity;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

import java.time.ZonedDateTime;

public class TestUtil {

    public static ProjectUsageIdentity getProjectUsageIdentity(Long detailsId, String monthYear) {
        final ProjectUsageIdentity identity = new ProjectUsageIdentity();
        identity.setProjectDetailsId(detailsId);
        identity.setMonthYear(monthYear);
        return identity;
    }

    public static ProjectUsageIdentity getProjectUsageIdentity() {
        return getProjectUsageIdentity(
                Long.parseLong(RandomStringUtils.randomNumeric(10)),
                RandomStringUtils.randomAlphanumeric(10));
    }

    public static ProjectUsageEntity getProjectUsageEntity(ProjectUsageIdentity identity, Integer monthlyUsage) {
        final ProjectUsageEntity entity = new ProjectUsageEntity();
        entity.setId(identity);
        entity.setMonthlyUsage(monthlyUsage);
        return entity;
    }

    public static ProjectUsageEntity getProjectUsageEntity() {
        return getProjectUsageEntity(getProjectUsageIdentity(), Integer.parseInt(RandomStringUtils.randomNumeric(4)));
    }

    public static ProjectDetailsEntity getProjectDetailsEntity(
            Long id, String organisationType, String organisationName, String projectName, String owner,
            ZonedDateTime dateCreated, boolean education, boolean serviceTool, String supportedBy) {
        final ProjectDetailsEntity entity = new ProjectDetailsEntity();
        entity.setId(id);
        entity.setOrganisationType(organisationType);
        entity.setOrganisationName(organisationName);
        entity.setProjectName(projectName);
        entity.setOwner(owner);
        entity.setDateCreated(dateCreated);
        entity.setEducation(education);
        entity.setServiceTool(serviceTool);
        entity.setSupportedBy(supportedBy);
        return entity;
    }

    public static ProjectDetailsEntity getProjectDetailsEntity() {
        return getProjectDetailsEntity(
                Long.parseLong(RandomStringUtils.randomNumeric(10)),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(5),
                RandomStringUtils.randomAlphanumeric(5),
                RandomStringUtils.randomAlphabetic(5),
                ZonedDateTime.now(),
                false,
                false,
                RandomStringUtils.randomAlphabetic(10)
        );
    }
}
