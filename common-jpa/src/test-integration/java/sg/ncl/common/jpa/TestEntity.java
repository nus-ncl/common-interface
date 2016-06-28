package sg.ncl.common.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Entity
public class TestEntity extends AbstractEntity {
    @GeneratedValue
    @Id
    private Long id;

}
