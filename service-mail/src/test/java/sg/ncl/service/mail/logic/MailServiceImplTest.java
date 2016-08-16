package sg.ncl.service.mail.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.service.mail.MailApplication;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Created by dcszwang on 8/10/2016.
 */
public class MailServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    JavaMailSender sender;

    MailServiceImpl serviceImpl;

    @Captor
    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

    @Before
    public void before() {
        serviceImpl = new MailServiceImpl(sender);
    }

    @Test
    public void testSend() {
        String from = RandomStringUtils.randomAlphanumeric(10);
        String to = RandomStringUtils.randomAlphanumeric(10);
        String subject = RandomStringUtils.randomAlphanumeric(10);
        String content = RandomStringUtils.randomAlphanumeric(10);
        serviceImpl.send(from, to, subject, content);

        Mockito.verify(sender).send(captor.capture());
        SimpleMailMessage value = captor.getValue();

        Assert.assertThat(value.getFrom(), is(equalTo(from)));
        Assert.assertThat(value.getTo().length, is(equalTo(1)));
        Assert.assertThat(value.getTo(), arrayContaining(to));
        Assert.assertThat(value.getSubject(), is(equalTo(subject)));
        Assert.assertThat(value.getText(), is(equalTo(content)));
    }
}
