package sg.ncl.service.mail.domain;

import sg.ncl.service.mail.data.jpa.EmailEntity;

/**
 * Created by dcsjnh on 24/4/2017.
 */
public interface AsyncMailSender {

    void send(EmailEntity emailEntity);

}
