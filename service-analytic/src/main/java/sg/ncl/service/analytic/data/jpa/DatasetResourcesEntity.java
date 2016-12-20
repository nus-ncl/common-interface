package sg.ncl.service.analytic.data.jpa;


import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytic.domain.DatasetResources;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;

/**
 * @author: Tran Ly Vu
 */

@Entity
@Table(name = "dataset_resources")
public class DatasetResourcesEntity extends AbstractEntity implements DatasetResources {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dataset_id")
    private DatasetEntity dataset;

    @Lob
    @Column(name = "url", length = 512)
    private String url;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }


    public DatasetEntity getDataset() {
        return dataset;
    }

    public void setDataset(final DatasetEntity dataset) {
        this.dataset = dataset;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id='" + id + '\'' +
                ", dataset_id='" + dataset + '\'' +
                ", url='" + url + '\'' +
                "} " + super.toString();
    }
}
