package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.AsyncMailService;
import sg.ncl.service.mail.domain.Email;
import sg.ncl.service.mail.domain.MailService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dcszwang on 8/10/2016.
 * Updated by dcsjnh on 24/4/2017
 *
 * References:
 * [1] http://stackoverflow.com/questions/24798695/spring-async-method-inside-a-service
 */
@Service
@Slf4j
class MailServiceImpl implements MailService {

    private final EmailRepository emailRepository;
    private final AsyncMailService asyncMailService;
    // FIXME should not use this format, instead should use auto configuration
    @Value("${ncl.mail.retry:3}")
    private int retryTimes;

    @Inject
    MailServiceImpl(@NotNull final EmailRepository emailRepository,
                    @NotNull final AsyncMailService asyncMailService) {
        this.emailRepository = emailRepository;
        this.asyncMailService = asyncMailService;
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
        entity.setSender(from);
        entity.setRecipients(to);
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setHtml(html);
        entity.setCc(cc);
        entity.setBcc(bcc);

        asyncMailService.send(entity);
    }

    @Transactional
    @Override
    public void send(@NotNull final Email email) {
        send(email.getSender(), email.getRecipients(), email.getSubject(), email.getContent(), email.isHtml(), email.getCc(), email.getBcc());
    }

    // FIXME should not use this format, instead should use auto configuration
    @Scheduled(initialDelayString = "${ncl.mail.delay:300000}", fixedRateString = "${ncl.mail.interval:600000}")
    @Transactional
    @Override
    public void retry() {
        final List<EmailEntity> emails = emailRepository.findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(3);
        log.info("Retrying {} emails", emails.size());
        emails.forEach(this::send);
    }

}
