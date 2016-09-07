package sg.ncl.service.mail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class MailAutoConfigurationTest {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private MailProperties mailProperties;

    private MailAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new MailAutoConfiguration(mailProperties);
    }

    @Test
    public void testDelayDurationDefault() {
        Duration delay = configuration.delayDuration();
        assertThat(delay, is(equalTo(MailAutoConfiguration.DEFAULT_DELAY)));
    }

    @Test
    public void testDelayDuration() {
        final String myDelay = "PT1M";
        when(mailProperties.getDelay()).thenReturn(myDelay);
        Duration delay = configuration.delayDuration();
        assertThat(delay.toMillis(), is(equalTo(Long.parseLong("60000"))));
    }

    @Test
    public void testIntervalDurationDefault() {
        Duration interval = configuration.intervalDuration();
        assertThat(interval, is(equalTo(MailAutoConfiguration.DEFAULT_INTERVAL)));
    }

    @Test
    public void testIntervalDuration() {
        final String myInterval = "PT1M";
        when(mailProperties.getInterval()).thenReturn(myInterval);
        Duration interval = configuration.intervalDuration();
        assertThat(interval.toMillis(), is(equalTo(Long.parseLong("60000"))));
    }
}
