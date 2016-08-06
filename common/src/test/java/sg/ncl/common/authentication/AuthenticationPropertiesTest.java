package sg.ncl.common.authentication;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
@SpringBootTest(classes = TestApp.class)
public class AuthenticationPropertiesTest extends AbstractTest {

    @Inject
    private AuthenticationProperties properties;

    @Test
    public void testGetUrl() throws Exception {
        assertThat(properties.getUrl(), is(equalTo("/authentications")));
    }

}
