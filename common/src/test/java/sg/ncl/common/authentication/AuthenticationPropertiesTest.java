package sg.ncl.common.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Chris on 8/6/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootApp.class)
public class AuthenticationPropertiesTest {

    @Inject
    private AuthenticationProperties properties;

    @Test
    public void testGetUrl() throws Exception {
        assertThat(properties.getUrl(), is(equalTo("/authentications")));
    }

}
