package sg.ncl.adapter.deterlab.dtos.entities;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.adapter.deterlab.domain.DeterLabUser;
import sg.ncl.common.jpa.AbstractEntity;

import javax.persistence.*;

/**
 * Created by Te Ye on 16-Jun-16.
 */
@Entity
@Table(name = "deterlab_user")
public class DeterLabUserEntity extends AbstractEntity implements DeterLabUser {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "ncl_user_id", nullable = false, unique = true)
    private String nclUserId;

    @Column(name = "deter_user_id", nullable = false, unique = true)
    private String deterUserId;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
