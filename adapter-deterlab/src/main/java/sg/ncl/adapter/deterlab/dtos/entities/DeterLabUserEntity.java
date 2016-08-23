package sg.ncl.adapter.deterlab.dtos.entities;

import sg.ncl.adapter.deterlab.domain.DeterLabUser;
import sg.ncl.common.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Te Ye on 16-Jun-16.
 */
@Entity
@Table(name = "deterlab_user")
public class DeterLabUserEntity extends AbstractEntity implements DeterLabUser {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ncl_user_id", nullable = false, unique = true)
    private String nclUserId;

    @Column(name = "deter_user_id", nullable = false, unique = true)
    private String deterUserId;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getNclUserId() {
        return nclUserId;
    }

    public void setNclUserId(String nclUserId) {
        this.nclUserId = nclUserId;
    }

    @Override
    public String getDeterUserId() {
        return deterUserId;
    }

    public void setDeterUserId(String deterUserId) {
        this.deterUserId = deterUserId;
    }
}
