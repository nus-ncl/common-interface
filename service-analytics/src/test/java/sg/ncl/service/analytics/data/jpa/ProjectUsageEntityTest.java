package sg.ncl.service.analytics.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectUsageEntityTest {

    @Test
    public void testGetId() {
        ProjectUsageEntity entity = new ProjectUsageEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        ProjectUsageEntity entity = new ProjectUsageEntity();
        ProjectUsageIdentity identity = new ProjectUsageIdentity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        String month = RandomStringUtils.randomAlphanumeric(10);
        identity.setProjectDetailsId(id);
        identity.setMonthYear(month);
        entity.setId(identity);
        assertThat(entity.getId().getProjectDetailsId()).isEqualTo(id);
        assertThat(entity.getId().getMonthYear()).isEqualTo(month);
    }

    @Test
    public void testGetMonthlyUsage() {
        ProjectUsageEntity entity = new ProjectUsageEntity();
        assertThat(entity.getMonthlyUsage()).isNull();
    }

    @Test
    public void testSetMonthlyUsage() {
        ProjectUsageEntity entity = new ProjectUsageEntity();
        Integer usage = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        entity.setMonthlyUsage(usage);
        assertThat(entity.getMonthlyUsage()).isEqualTo(usage);
    }
}
