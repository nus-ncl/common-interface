package sg.ncl.service.mail.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mail.javamail.JavaMailSender;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.AsyncMailSender;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Created by dcsjnh on 24/4/2017.
 */
public class AsyncMailSenderImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailRepository emailRepository;

    private AsyncMailSender service;

    @Before
    public void before() {
        service = new AsyncMailSenderImpl(javaMailSender, emailRepository);
    }

    @Test
    public void testSendGood() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";

        final EmailEntity entity = new EmailEntity();
        entity.setSender(from);
        entity.setRecipients(new String[]{to});
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setHtml(false);

        final MimeMessage message = new MimeMessage((Session) null);
        doReturn(message).when(javaMailSender).createMimeMessage();

        service.send(entity);

        String[] recipients = entity.getRecipients();
        assertThat(message.getFrom().length).isEqualTo(1);
        assertThat(message.getFrom()).containsOnly(new InternetAddress(entity.getSender()));
        assertThat(message.getAllRecipients().length).isEqualTo(1);
        assertThat(message.getAllRecipients()).containsOnly(new InternetAddress(recipients[0]));
        assertThat(message.getRecipients(Message.RecipientType.TO).length).isEqualTo(1);
        assertThat(message.getRecipients(Message.RecipientType.CC)).isNull();
        assertThat(message.getRecipients(Message.RecipientType.BCC)).isNull();
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendBad() throws Exception {
        final String from = "";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";

        final EmailEntity entity = new EmailEntity();
        entity.setSender(from);
        entity.setRecipients(new String[]{to});
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setHtml(false);

        final MimeMessage message = new MimeMessage((Session) null);
        doReturn(message).when(javaMailSender).createMimeMessage();

        service.send(entity);
    }
}
