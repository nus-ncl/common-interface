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
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/10/2016.
 */

@Service
@Slf4j
class MailServiceImpl implements MailService {

    // max retry times for each email
    private static final int MAX_RETRY_TIMES = 3;
    // retry interval for each email, in seconds
    private static final int RETRY_INTERVAL_PER_EMAIL = 3600;

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;

    @Inject
    MailServiceImpl(
            @NotNull final JavaMailSender javaMailSender,
            @NotNull final EmailRepository emailRepository
    ) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
    }

    @Transactional
    @Override
    public void send(
            @NotNull final String from,
            @NotNull final String to,
            @NotNull final String subject,
            @NotNull final String content,
            @NotNull final boolean html,
            final String cc,
            final String bcc
    ) {
        send(from, new String[]{to}, subject, content, html, cc == null ? null : new String[]{cc}, bcc == null ? null : new String[]{bcc});
    }

    @Transactional
    @Override
    public void send(
            @NotNull final String from,
            @NotNull final String[] to,
            @NotNull final String subject,
            @NotNull final String content,
            @NotNull final boolean html,
            final String[] cc,
            final String[] bcc
    ) {
        final EmailEntity entity = new EmailEntity();
        entity.setFrom(from);
        entity.setTo(to);
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setHtml(html);
        entity.setCc(cc);
        entity.setBcc(bcc);

        send(entity);
    }

    @Transactional
    @Override
    public void send(@NotNull final Email email) {
        send(email.getFrom(), email.getTo(), email.getSubject(), email.getContent(), email.isHtml(), email.getCc(), email.getBcc());
    }

    private void send(@NotNull final EmailEntity entity) {
        final MimeMessage message = prepareMessage(entity);
        entity.setLastRetryTime(ZonedDateTime.now());
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
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
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

//    @Scheduled(fixedDelay = 5000)
//    @Transactional
//    public void resend() {
//        EmailEntity entity = findEmailForRetry();
//        if (entity != null) {
//            MimeMessage message = prepareMessage(entity);
//            entity.setLastRetryTime(ZonedDateTime.now());
//            entity.setRetryTimes(entity.getRetryTimes() + 1);
//            send(entity, message);
//            emailRepository.save(entity);
//        }
//    }
//
//    private EmailEntity findEmailForRetry() {
//        List<EmailEntity> emailEntityList =
//                emailRepository.findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(MAX_RETRY_TIMES);
//        if (!emailEntityList.isEmpty()) {
//            EmailEntity candidate = emailEntityList.get(0);
//            return (ZonedDateTime.now().toEpochSecond() - candidate.getLastRetryTime().toEpochSecond()) >= RETRY_INTERVAL_PER_EMAIL ?
//                    candidate : null;
//        } else {
//            return null;
//        }
//    }

}
