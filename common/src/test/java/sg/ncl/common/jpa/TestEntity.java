package sg.ncl.common.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Chris on 8/7/2016.
 */
@Entity
class TestEntity extends AbstractEntity {
    @GeneratedValue
    @Id
    private Long id;
}
