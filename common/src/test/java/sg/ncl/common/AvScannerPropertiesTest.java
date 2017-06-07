package sg.ncl.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.AvScannerPropertiesTest.TestConfig;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by teye
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = {
        "ncl.av-scanner.cron=* 0 2 * * SUN",
        "ncl.av-scanner.host=123.123.123.123",
        "ncl.av-scanner.port=1234"
})
public class AvScannerPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(AvScannerProperties.class)
    static class TestConfig {
    }

    @Inject
    private AvScannerProperties properties;

    @Test
    public void testGetHost() throws Exception {
        assertThat(properties.getHost()).isEqualTo("123.123.123.123");
    }

    @Test
    public void testGetPort() throws Exception {
        assertThat(properties.getPort()).isEqualTo(1234);
    }

    @Test
    public void testGetCron() throws Exception {
        assertThat(properties.getCron()).isEqualTo("* 0 2 * * SUN");
    }

}
