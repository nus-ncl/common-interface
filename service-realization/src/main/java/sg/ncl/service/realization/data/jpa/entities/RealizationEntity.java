package sg.ncl.service.realization.data.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.service.realization.domain.Realization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "users")
public class RealizationEntity extends AbstractEntity implements Realization {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Realization that = (Realization) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RealizationEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
