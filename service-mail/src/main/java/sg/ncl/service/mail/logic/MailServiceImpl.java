package sg.ncl.service.mail.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sg.ncl.service.mail.domain.MailService;


import javax.inject.Inject;
import javax.validation.constraints.NotNull;



/**
 * Created by dcszwang on 8/10/2016.
 */

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final String from = "testbed-ops@ncl.sg";
    private final JavaMailSender sender;

    @Inject
    MailServiceImpl(
            @NotNull final JavaMailSender sender
    ) {
        this.sender = sender;
    }

    @Override
    public void send(
            @NotNull final String from,
            @NotNull final String to,
            @NotNull final String subject,
            @NotNull final String content
    ) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);

        try {
            sender.send(msg);
            log.info("Email sent: {}", msg);
        } catch (MailException e) {
            log.warn("{}: msg = {}", e, msg);
        }
    }
}
