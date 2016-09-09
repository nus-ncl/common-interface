package sg.ncl.service.mail.util;

import sg.ncl.service.mail.data.jpa.EmailEntity;

import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class TestUtil {
    public static EmailEntity getEmailEntity() {
        return getEmailEntity("admin@ncl.sg", new String[]{"user@ncl.sg"}, "subject", "content", false);
    }

    public static EmailEntity getEmailEntity(final String sender, final String[] recipients,
                                             final String subject, final String content, final boolean html) {
        final EmailEntity entity = new EmailEntity();

        entity.setSender(sender);
        entity.setRecipients(recipients);
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setHtml(html);
        entity.setLastRetryTime(ZonedDateTime.now());
        return entity;
    }
}
