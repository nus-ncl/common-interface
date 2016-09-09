package sg.ncl.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by dcszwang on 9/7/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MailPropertiesTest {
    @Inject
    private MailProperties mailProperties;

    @Test
    public void testDelay() throws Exception {
        assertThat(mailProperties.getDelay(), is(equalTo("300000")));
    }

    @Test
    public void testInterval() throws Exception {
        assertThat(mailProperties.getInterval(), is(equalTo("600000")));
    }

    @Test
    public void testRetry() throws Exception {
        assertThat(mailProperties.getRetry(), is(equalTo("3")));
    }
}
