package sg.ncl.service.upload;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by dcsjnh on 11/23/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DirectoryPropertiesTest.TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "upload.baseDir=uploads",
        "upload.subDirs.dataDir=dataresources"
})
public class DirectoryPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(DirectoryProperties.class)
    static class TestApplication {}

    @Inject
    private DirectoryProperties directoryProperties;

    @Test
    public void testBaseDirectory() {
        assertThat(directoryProperties.getBaseDir(), is(equalTo("uploads")));
    }

    @Test
    public void testSubDirectory() {
        assertThat(directoryProperties.getSubDirs(), anyOf(hasEntry("dataDir", "dataresources")));
    }
}
