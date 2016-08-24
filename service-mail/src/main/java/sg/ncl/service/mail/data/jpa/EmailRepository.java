package sg.ncl.service.mail.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by dcszwang on 8/23/2016.
 */
public interface EmailRepository extends JpaRepository<EmailEntity, String> {
}
