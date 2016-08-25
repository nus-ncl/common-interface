package sg.ncl.service.mail.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.mail.domain.Email;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/23/2016.
 */
@Entity
@Table(name = "email_retries")
@Getter
@Setter
public class EmailEntity extends AbstractEntity implements Email {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "sender", nullable = false, updatable = false)
    private InternetAddress sender;

    @Column(name = "receipts", nullable = false, updatable = false)
    private InternetAddress[] receipts;

    @Column(name = "cc_list", updatable = false)
    private InternetAddress[] ccList;

    @Column(name = "subject", nullable = false, updatable = false)
    private String subject;

    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @Column(name = "html", nullable = false, updatable = false)
    private boolean html;

    @Column(name = "retry_times", nullable = false)
    private int retryTimes = 0;

    @Column(name = "last_retry_time", nullable = false)
    private ZonedDateTime lastRetryTime;

    @Column(name = "error_message")
    private String errorMessage = null;

    @Column(name = "sent", nullable = false)
    private boolean sent = false;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Email that = (Email) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "EmailEntity{" +
                "id=" + id +
                ", sender=" + sender +
                ", receipts=" + receipts +
                ", cc_list=" + ccList +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", html=" + html +
                ", retryTimes=" + retryTimes +
                ", lastRetryTime=" + lastRetryTime +
                ", errorMessage='" + errorMessage + '\'' +
                ", sent=" + sent +
                "} " + super.toString();
    }
}
