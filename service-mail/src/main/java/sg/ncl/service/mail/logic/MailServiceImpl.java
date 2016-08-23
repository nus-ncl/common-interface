package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sg.ncl.service.mail.data.jpa.EmailRetriesEntity;
import sg.ncl.service.mail.data.jpa.EmailRetriesRepository;
import sg.ncl.service.mail.domain.MailService;


import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;


/**
 * Created by dcszwang on 8/10/2016.
 */

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final String from = "testbed-ops@ncl.sg";
    private final JavaMailSender sender;
    private EmailRetriesRepository emailRetriesRepository;

    @Inject
    MailServiceImpl(
            @NotNull final JavaMailSender sender,
            @NotNull final EmailRetriesRepository emailRetriesRepository
    ) {
        this.sender = sender;
        this.emailRetriesRepository = emailRetriesRepository;
    }

    @Override
    public void send(
            @NotNull final String from,
            @NotNull final String to,
            @NotNull final String subject,
            @NotNull final String content
    ) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException e) {
            log.warn("{}: message = {}", e, message);
            return;
        }
        try {
            sender.send(message);
            log.info("Email sent: {}", message);
        } catch (MailException me) {
            log.warn("{}: message = {}", me, message);
            EmailRetriesEntity emailRetriesEntity = new EmailRetriesEntity();
            emailRetriesEntity.setMessage(message);
            emailRetriesEntity.setRetriedTimes(0);
            emailRetriesEntity.setLastRetryTime(ZonedDateTime.now());
            emailRetriesEntity.setErrInfo(me.getMessage());

        }
    }
}
