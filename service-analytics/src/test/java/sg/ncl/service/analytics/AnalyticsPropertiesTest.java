package sg.ncl.service.analytics;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;

/**
 * @Author: Tran Ly Vu
 */

@RunWith(SpringRunner.class)
@EnableConfigurationProperties(AnalyticsProperties.class)
@TestPropertySource(properties = {
        "ncl.analytics.energyDir=~/energylog",
})
public class AnalyticsPropertiesTest {

    @Inject
    private AnalyticsProperties analyticsProperties;

    public void before() {
        assertThat(mockingDetails(analyticsProperties).isMock()).isTrue();
    }
    @Test
    public void testGetEnergyDir() throws Exception {
        assertThat(analyticsProperties.getEnergyDir()).isEqualTo("~/energylog");
    }

}
