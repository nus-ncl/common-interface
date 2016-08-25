package sg.ncl.service.mail.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by dcszwang on 8/23/2016.
 */
public interface EmailRepository extends JpaRepository<EmailEntity, String> {
    List<EmailEntity> findBySentFalseAndRetryTimesLessThanOrderByRetryTimes(int maxRetryTimes);
}
