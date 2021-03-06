package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataCategory;

import javax.persistence.*;

/**
 * Created by dcsjnh on 9/5/2017.
 */
@Entity
@Table(name = "data_categories")
@Getter
@Setter
public class DataCategoryEntity extends AbstractEntity implements DataCategory {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

}
