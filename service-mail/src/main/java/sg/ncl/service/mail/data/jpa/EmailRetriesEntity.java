package sg.ncl.service.mail.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.mail.domain.EmailRetries;

import javax.mail.Message;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/23/2016.
 */
@Entity
@Table(name = "email_retries")
public class EmailRetriesEntity extends AbstractEntity implements EmailRetries {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "message", nullable = false)
    private Message message;

    @Column(name = "retried_times", nullable = false)
    private int retriedTimes;

    @Column(name = "last_retry_time", nullable = false)
    private ZonedDateTime lastRetryTime;

    @Column(name = "err_info", nullable = true)
    private String errInfo;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public int getRetriedTimes() {
        return retriedTimes;
    }

    public void setRetriedTimes(int retriedTimes) {
        this.retriedTimes = retriedTimes;
    }

    @Override
    public ZonedDateTime getLastRetryTime() {
        return lastRetryTime;
    }

    public void setLastRetryTime(ZonedDateTime lastRetryTime) {
        this.lastRetryTime = lastRetryTime;
    }

    @Override
    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EmailRetries that = (EmailRetries) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "EmailRetriesEntity{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", retried_times='" + retriedTimes + '\'' +
                ", last_retry_time=" + lastRetryTime +
                ", err_info=" + errInfo +
                "} " + super.toString();
    }
}
