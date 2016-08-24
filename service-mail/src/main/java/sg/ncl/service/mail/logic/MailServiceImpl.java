package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.Email;
import sg.ncl.service.mail.domain.MailService;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;


/**
 * Created by dcszwang on 8/10/2016.
 */

@Service
@Slf4j
class MailServiceImpl implements MailService {

    private final JavaMailSender sender;
    private final EmailRepository emailRepository;

    @Inject
    MailServiceImpl(
            @NotNull final JavaMailSender sender,
            @NotNull final EmailRepository emailRepository
    ) {
        this.sender = sender;
        this.emailRepository = emailRepository;
    }

    @Transactional
    @Override
    public void send(
            @NotNull final InternetAddress from,
            @NotNull final InternetAddress to,
            @NotNull final String subject,
            @NotNull final String content,
            @NotNull final boolean isHtml
    ) {
        final EmailEntity emailEntity = new EmailEntity();
        emailEntity.setFrom(from);
        emailEntity.setTo(to);
        emailEntity.setSubject(subject);
        emailEntity.setContent(content);
        final MimeMessage message = prepareMessage(emailEntity);
        send(emailEntity, message);
        emailRepository.save(emailEntity);
    }

    private void send(final EmailEntity emailEntity, final MimeMessage message) {
        emailEntity.setLastRetryTime(ZonedDateTime.now());
        try {
            sender.send(message);
            log.info("Email sent: {}", message);
            emailEntity.setSent(true);
            emailEntity.setErrorMessage(null);
        } catch (MailException e) {
            log.warn("{}: message = {}", e, message);
            emailEntity.setErrorMessage(e.getMessage());
        }
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
