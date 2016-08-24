package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.Email;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/24/2016.
 */
@Slf4j
@EnableScheduling
public class EmailRetriesImpl {
    private final JavaMailSender sender;
    private final EmailRepository emailRepository;

    @Inject
    EmailRetriesImpl(final JavaMailSender sender, EmailRepository emailRepository) {
        this.sender = sender;
        this.emailRepository = emailRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void retrySendEmail() {
        EmailEntity one = findAnEmailForRetry();
        MimeMessage message = prepareMessage(one);
        one.setLastRetryTime(ZonedDateTime.now());

        try {
            sender.send(message);
            log.info("Email sent: {}", message);
            one.setSent(true);
            one.setErrorMessage(null);
        } catch (MailException e) {
            log.warn("{}: message = {}", e, message);
            one.setErrorMessage(e.getMessage());
        }
    }


    private EmailEntity findAnEmailForRetry() {
        return null;
    }

    private MimeMessage prepareMessage(final Email email) {
        final MimeMessage message = sender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), email.isHtml());
        } catch (MessagingException e) {
            log.warn("{}: message = {}", e, message);
            throw new IllegalArgumentException(e);
        }
        return message;
    }
}
