package sg.ncl.adapter.deterlab.dtos.entities;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
}
