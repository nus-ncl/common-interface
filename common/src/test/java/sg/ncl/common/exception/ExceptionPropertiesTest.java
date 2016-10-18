package sg.ncl.common.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Created by Chris on 8/7/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootApp.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = "flyway.enabled=false")
public class ExceptionPropertiesTest {

    @Inject
    private ExceptionProperties properties;

    @Test
    public void testGetMappings() throws Exception {
        final Map<String, String> mappings = properties.getMappings();

        assertThat(mappings.size(), is(equalTo(1)));
        assertThat(mappings.get("mapping1"), is(equalTo("value1")));
    }

}
