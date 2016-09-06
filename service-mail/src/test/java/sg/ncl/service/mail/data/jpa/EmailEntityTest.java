package sg.ncl.service.mail.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by dcszwang on 9/6/2016.
 */
public class EmailEntityTest {

    @Test
    public void testGetId() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() {
        EmailEntity emailEntity = new EmailEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        emailEntity.setId(id);
        assertThat(emailEntity.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetSender() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getSender(), is(nullValue()));
    }

    @Test
    public void testSetSender() {
        EmailEntity emailEntity = new EmailEntity();
        final String sender = "alice@ncl.sg";
        emailEntity.setSender(sender);
        assertThat(emailEntity.getSender(), is(equalTo(sender)));
    }

    @Test
    public void testGetRecipients() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getRecipients(), is(nullValue()));
    }

    @Test
    public void testSetRecipients() {
        EmailEntity emailEntity = new EmailEntity();
        final String recipients = "alice@ncl.sg";
        emailEntity.setRecipients(new String[] {recipients});
        final String[] to = emailEntity.getRecipients();
        assertThat(to.length, is(equalTo(1)));
        assertThat(to[0], is(equalTo(recipients)));
    }

    @Test
    public void testGetCc() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getCc(), is(nullValue()));
    }

    @Test
    public void testSetCc() {
        EmailEntity emailEntity = new EmailEntity();
        final String cc = "alice@ncl.sg";
        emailEntity.setCc(new String[] {cc});
        final String[] cc2 = emailEntity.getCc();
        assertThat(cc2.length, is(equalTo(1)));
        assertThat(cc2[0], is(equalTo(cc)));
    }

    @Test
    public void testGetBcc() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getBcc(), is(nullValue()));
    }

    @Test
    public void testSetBcc() {
        EmailEntity emailEntity = new EmailEntity();
        final String bccString = "alice@ncl.sg";
        emailEntity.setBcc(new String[] {bccString});
        final String[] bcc = emailEntity.getBcc();
        assertThat(bcc.length, is(equalTo(1)));
        assertThat(bcc[0], is(equalTo(bccString)));
    }

    @Test
    public void testGetSubject() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getSubject(), is(nullValue()));
    }

    @Test
    public void testSetSubject() {
        EmailEntity emailEntity = new EmailEntity();
        final String subString = "subject";
        emailEntity.setSubject(subString);
        assertThat(emailEntity.getSubject(), is(equalTo(subString)));
    }

    @Test
    public void testGetContent() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getContent(), is(nullValue()));
    }

    @Test
    public void testSetContent() {
        EmailEntity emailEntity = new EmailEntity();
        final String content = "content";
        emailEntity.setContent(content);
        assertThat(emailEntity.getContent(), is(equalTo(content)));
    }

    @Test
    public void testGetHtml() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.isHtml(), is(false));
    }

    @Test
    public void testSetHtml() {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setHtml(false);
        assertThat(emailEntity.isHtml(), is(equalTo(false)));
    }

    @Test
    public void testGetRetryTimes() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getRetryTimes(), is(-1));
    }

    @Test
    public void testSetRetryTimes() {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setRetryTimes(1);
        assertThat(emailEntity.getRetryTimes(), is(equalTo(1)));
    }

    @Test
    public void testGetLastRetryTime() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getLastRetryTime(), is(nullValue()));
    }

    @Test
    public void testSetLastRetryTime() {
        EmailEntity emailEntity = new EmailEntity();
        ZonedDateTime time = ZonedDateTime.now();
        emailEntity.setLastRetryTime(time);
        assertThat(emailEntity.getLastRetryTime(), is(equalTo(time)));
    }

    @Test
    public void testGetErrorMessage() {
        EmailEntity emailEntity = new EmailEntity();
        assertThat(emailEntity.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void testSetErrorMessage() {
        EmailEntity emailEntity = new EmailEntity();
        final String error = "error message";
        emailEntity.setErrorMessage(error);
        assertThat(emailEntity.getErrorMessage(), is(equalTo(error)));
    }
}
