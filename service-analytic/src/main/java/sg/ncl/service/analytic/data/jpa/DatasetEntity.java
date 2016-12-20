package sg.ncl.service.analytic.data.jpa;


import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytic.domain.Dataset;

import javax.persistence.*;


/**
 * @author: Tran Ly Vu
 */

@Entity
@Table(name = "datasets")
public class DatasetEntity extends AbstractEntity implements Dataset {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique =true)
    private String name;

    @Lob
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "accessibility", nullable = false)
    private String accessibility;

    @Column(name = "visibility", nullable = false)
    private String visibility;

    @Column(name = "contributor_id", nullable = false)
    private String contributor_id;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(final String accessibility) {
        this.accessibility = accessibility;
    }


    @Override
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String getContributorId() {
        return contributor_id;
    }

    public void setContributorId(final String contributor_id) {
        this.contributor_id = contributor_id;
    }

    @Override
    public String toString() {
        return "DatasetEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", accesssibility='" + accessibility + '\'' +
                ", visibility='" + visibility + '\'' +
                ", contributor_id='" + contributor_id + '\'' +
                "} " + super.toString();
    }

}
