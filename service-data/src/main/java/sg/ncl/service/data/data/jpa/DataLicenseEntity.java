package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataLicense;

import javax.persistence.*;

/**
 * Created by dcsjnh on 30/5/2017.
 */
@Entity
@Table(name = "data_licenses")
@Getter
@Setter
public class DataLicenseEntity extends AbstractEntity implements DataLicense {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "acronym", nullable = false, updatable = true)
    private String acronym;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @Type(type = "text")
    @Column(name = "link")
    private String link;

}
