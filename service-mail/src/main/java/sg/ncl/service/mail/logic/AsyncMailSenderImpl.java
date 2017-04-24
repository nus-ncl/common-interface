package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.AsyncMailSender;
import sg.ncl.service.mail.domain.Email;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Created by dcsjnh on 24/4/2017.
 *
 * References:
 * [1] http://stackoverflow.com/questions/24798695/spring-async-method-inside-a-service
 */
@Service
@Slf4j
public class AsyncMailSenderImpl implements AsyncMailSender {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;

    @Inject
    AsyncMailSenderImpl(@NotNull final JavaMailSender javaMailSender,
                        @NotNull final EmailRepository emailRepository) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
    }

    @Async
    public void send(@NotNull final EmailEntity entity) {
        final MimeMessage message = prepareMessage(entity);
        entity.setLastRetryTime(ZonedDateTime.now());
        entity.setRetryTimes(entity.getRetryTimes() + 1);
        try {
            javaMailSender.send(message);
            log.info("Email sent: {}", message);
            entity.setSent(true);
            entity.setErrorMessage(null);
        } catch (MailException e) {
            log.warn("{}", e);
            entity.setErrorMessage(e.getMessage());
        }
        emailRepository.save(entity);
    }

    private MimeMessage prepareMessage(final Email email) {
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(email.getSender());
            helper.setTo(email.getRecipients());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), email.isHtml());
            final String[] cc = email.getCc();
            if (cc != null) {
                helper.setCc(cc);
            }
            if (email.getBcc() != null) {
                helper.setBcc(email.getBcc());
            }
        } catch (MessagingException e) {
            log.warn("{}", e);
            throw new IllegalArgumentException(e);
        }
        return message;
    }
}
