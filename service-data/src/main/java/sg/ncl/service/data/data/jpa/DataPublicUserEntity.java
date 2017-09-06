package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataPublicUser;

import javax.persistence.*;

@Entity
@Table(name = "data_public_users")
@Getter
@Setter
public class DataPublicUserEntity extends AbstractEntity implements DataPublicUser {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "country", nullable = false)
    private String country;

}
