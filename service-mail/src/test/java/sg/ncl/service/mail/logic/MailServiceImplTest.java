package sg.ncl.service.mail.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.AsyncMailService;
import sg.ncl.service.mail.domain.MailService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by dcszwang on 8/10/2016.
 */
public class MailServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private EmailRepository emailRepository;
    @Mock
    private AsyncMailService asyncMailService;

    private MailService service;

    @Before
    public void before() {
        service = new MailServiceImpl(emailRepository, asyncMailService);
    }

    @Test
    public void testSendGood() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String subject = "subject";
        final String content = "content";

        service.send(from, to, subject, content, false, null, null);

        verify(asyncMailService, times(1)).send(any(EmailEntity.class));
    }

    @Test
    public void testSendGoodWithCcAndBcc() throws Exception {
        final String from = "alice@ncl.sg";
        final String to = "bob@ncl.sg";
        final String cc = "admin@ncl.sg";
        final String bcc = "logs@ncl.sg";
        final String subject = "subject";
        final String content = "content";

        service.send(from, to, subject, content, false, cc, bcc);

        verify(asyncMailService, times(1)).send(any(EmailEntity.class));
    }

    @Test
    public void testRetry() {
        final EmailEntity entity1 = new EmailEntity();
        entity1.setSender("alice@ncl.sg");
        entity1.setRecipients(new String[]{"bob@ncl.sg"});
        entity1.setSubject("subject 1");
        entity1.setContent("content");
        entity1.setHtml(false);

        final EmailEntity entity2 = new EmailEntity();
        entity2.setSender("alice@ncl.sg");
        entity2.setRecipients(new String[]{"bob@ncl.sg"});
        entity2.setSubject("subject 2");
        entity2.setContent("content");
        entity2.setHtml(false);

        final List<EmailEntity> emails = new ArrayList<EmailEntity>();
        emails.add(entity1);
        emails.add(entity2);

        when(emailRepository.findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(3)).thenReturn(emails);

        service.retry();

        verify(asyncMailService, times(2)).send(any(EmailEntity.class));
    }
}
