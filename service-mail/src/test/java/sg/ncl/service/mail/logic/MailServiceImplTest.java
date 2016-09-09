package sg.ncl.service.mail.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mail.javamail.JavaMailSender;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.MailService;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by dcszwang on 8/10/2016.
 */
public class MailServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailRepository emailRepository;

    private MailService service;

    @Before
    public void before() {
        service = new MailServiceImpl(javaMailSender, emailRepository);
    }

    @Test
    public void testSendGood() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";
        final MimeMessage message = new MimeMessage((Session) null);

        doReturn(message).when(javaMailSender).createMimeMessage();

        service.send(from, to, subject, content, false, null, null);

        assertThat(message.getFrom().length).isEqualTo(1);
        assertThat(message.getFrom()).containsOnly(new InternetAddress(from));
        assertThat(message.getAllRecipients().length).isEqualTo(1);
        assertThat(message.getAllRecipients()).containsOnly(new InternetAddress(to));
        assertThat(message.getRecipients(RecipientType.TO).length).isEqualTo(1);
        assertThat(message.getRecipients(RecipientType.CC)).isNull();
        assertThat(message.getRecipients(RecipientType.BCC)).isNull();
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    public void testSendGoodWithCcAndBcc() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String cc = "admin@ncl.sg";
        final String bcc = "logs@ncl.sg";
        final String subject = "subject";
        final String content = "content";
        final MimeMessage message = new MimeMessage((Session) null);

        doReturn(message).when(javaMailSender).createMimeMessage();

        service.send(from, to, subject, content, false, cc, bcc);

        assertThat(message.getFrom().length).isEqualTo(1);
        assertThat(message.getFrom()).containsOnly(new InternetAddress(from));
        assertThat(message.getAllRecipients().length).isEqualTo(3);
        assertThat(message.getRecipients(RecipientType.TO).length).isEqualTo(1);
        assertThat(message.getRecipients(RecipientType.TO)).containsOnly(new InternetAddress(to));
        assertThat(message.getRecipients(RecipientType.CC).length).isEqualTo(1);
        assertThat(message.getRecipients(RecipientType.CC)).containsOnly(new InternetAddress(cc));
        assertThat(message.getRecipients(RecipientType.BCC).length).isEqualTo(1);
        assertThat(message.getRecipients(RecipientType.BCC)).containsOnly(new InternetAddress(bcc));
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendBad() throws Exception {
        final String from = "";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";
        final MimeMessage message = new MimeMessage((Session) null);

        doReturn(message).when(javaMailSender).createMimeMessage();

        service.send(from, to, subject, content, false, null, null);
    }

    @Test
    public void testSendEmailGood() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";

        final EmailEntity entity = new EmailEntity();
        entity.setSender(from);
        entity.setRecipients(new String[] {to});
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
        assertThat(message.getRecipients(RecipientType.TO).length).isEqualTo(1);
        assertThat(message.getRecipients(RecipientType.CC)).isNull();
        assertThat(message.getRecipients(RecipientType.BCC)).isNull();
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    public void testRetry() {
        final EmailEntity entity1 = new EmailEntity();
        entity1.setSender("alice@ncl.sg");
        entity1.setRecipients(new String[] {"bob@ncl.sg"});
        entity1.setSubject("subject 1");
        entity1.setContent("content");
        entity1.setHtml(false);

        final EmailEntity entity2 = new EmailEntity();
        entity2.setSender("alice@ncl.sg");
        entity2.setRecipients(new String[] {"bob@ncl.sg"});
        entity2.setSubject("subject 2");
        entity2.setContent("content");
        entity2.setHtml(false);

        final List<EmailEntity> emails = new ArrayList<EmailEntity>();
        emails.add(entity1);
        emails.add(entity2);

        final MimeMessage message = new MimeMessage((Session) null);

        when(javaMailSender.createMimeMessage()).thenReturn(message);
        when(emailRepository.findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(3)).thenReturn(emails);

        service.retry();
    }
}
