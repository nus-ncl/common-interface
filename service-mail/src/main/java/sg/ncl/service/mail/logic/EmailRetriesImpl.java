package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.mail.data.jpa.EmailEntity;
import sg.ncl.service.mail.data.jpa.EmailRepository;
import sg.ncl.service.mail.domain.MailService;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dcszwang on 8/24/2016.
 */
@Component
@Slf4j
public class EmailRetriesImpl {
    private final MailService mailService;
    private final EmailRepository emailRepository;
    private static final int MAXRETRYTIMES = 3;

    @Inject
    EmailRetriesImpl(
            final MailService mailService,
            final EmailRepository emailRepository
    ) {
        this.mailService = mailService;
        this.emailRepository = emailRepository;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void resend() {
        EmailEntity one = findEmailForRetry();
        if(one != null) {
            MimeMessage message = mailService.prepareMessage(one);
            one.setLastRetryTime(ZonedDateTime.now());
            one.setRetryTimes(one.getRetryTimes() + 1);
            mailService.send(one, message);
            emailRepository.save(one);
        }
    }


    private EmailEntity findEmailForRetry() {
        List<EmailEntity> emailEntityList =
                emailRepository.findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(MAXRETRYTIMES);
        if(emailEntityList.size() > 0) {
            return emailEntityList.get(0);
        } else {
            return null;
        }
    }

}
