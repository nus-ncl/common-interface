package sg.ncl.service.experiment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Desmond.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExperimentConnectionPropertiesTest.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "deterurl.bossurl=boss.ncl.sg",
        "deterurl.userurl=users.ncl.sg",
        "deterurl.port=8080"
})
public class ExperimentConnectionPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(ExperimentConnectionProperties.class)
    static class TestConfig {
    }

    @Inject
    private ExperimentConnectionProperties experimentConnectionProperties;

    @Test
    public void testGetBossUrlReturnsNotNull() throws Exception {
        assertThat(experimentConnectionProperties.getBossurl()).isNotNull();
        assertThat(experimentConnectionProperties.getBossurl()).isEqualTo("boss.ncl.sg");
    }

    @Test
    public void testGetUserUrlReturnsNotNull() throws Exception {
        assertThat(experimentConnectionProperties.getUserurl()).isNotNull();
        assertThat(experimentConnectionProperties.getUserurl()).isEqualTo("users.ncl.sg");
    }

    @Test
    public void testGetPortReturnsNotNull() throws Exception {
        assertThat(experimentConnectionProperties.getPort()).isNotNull();
        assertThat(experimentConnectionProperties.getPort()).isEqualTo("8080");
    }
}
