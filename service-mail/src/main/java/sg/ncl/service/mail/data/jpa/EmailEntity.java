package sg.ncl.service.mail.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.mail.domain.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Arrays;

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
    private String sender;

    @Column(name = "recipients", nullable = false, updatable = false)
    private String[] recipients;

    @Column(name = "cc", updatable = false)
    private String[] cc = null;

    @Column(name = "bcc", updatable = false)
    private String[] bcc = null;

    @Column(name = "subject", nullable = false, updatable = false)
    private String subject;

    @Type(type = "text")
    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @Column(name = "html", nullable = false, updatable = false)
    private boolean html;

    @Column(name = "retry_times", nullable = false)
    private int retryTimes = -1;

    @Column(name = "last_retry_time", nullable = false)
    private ZonedDateTime lastRetryTime;

    @Type(type = "text")
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

        final EmailEntity that = (EmailEntity) o;

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
                ", sender='" + sender + '\'' +
                ", recipients=" + Arrays.toString(recipients) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
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
