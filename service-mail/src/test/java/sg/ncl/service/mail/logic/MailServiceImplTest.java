package sg.ncl.service.mail.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mail.javamail.JavaMailSender;
import sg.ncl.service.mail.data.jpa.EmailRepository;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by dcszwang on 8/10/2016.
 */
public class MailServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    JavaMailSender sender;
    @Mock
    EmailRepository emailRepository;

    MailServiceImpl serviceImpl;

    @Captor
    ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);

    @Before
    public void before() {
        serviceImpl = new MailServiceImpl(sender, emailRepository);
    }

    @Test
    public void testSend() throws MessagingException, IOException {
        InternetAddress from = new InternetAddress("alice@ncl.sg");
        InternetAddress to = new InternetAddress("bob@ncl.sg");
        String subject = RandomStringUtils.randomAlphanumeric(10);
        String content = RandomStringUtils.randomAlphanumeric(10);
        Session session = null;
        MimeMessage message = new MimeMessage(session);
        Mockito.doReturn(message).when(sender).createMimeMessage();
        serviceImpl.send(from, to, subject, content, false);

        Mockito.verify(sender).send(captor.capture());
        MimeMessage value = captor.getValue();

        Address[] fromArray = value.getFrom();
        Address[] toArray = value.getAllRecipients();
        Assert.assertThat(fromArray.length, is(equalTo(1)));
        Assert.assertThat(toArray.length, is(equalTo(1)));
        Assert.assertThat(fromArray, arrayContaining(from));
        Assert.assertThat(toArray, arrayContaining(to));
        Assert.assertThat(value.getSubject(), is(equalTo(subject)));
        Assert.assertThat(value.getContentType(), is(equalTo("text/plain")));
        Assert.assertThat(value.getContent().toString(), is(equalTo(content)));
    }
}
