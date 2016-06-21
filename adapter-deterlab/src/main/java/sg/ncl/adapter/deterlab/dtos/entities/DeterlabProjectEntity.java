package sg.ncl.adapter.deterlab.dtos.entities;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.adapter.deterlab.domain.DeterlabProject;
import sg.ncl.common.jpa.AbstractEntity;

import javax.persistence.*;

/**
 * Created by Te Ye on 16-Jun-16.
 */
@Entity
@Table(name = "deterlab_project")
public class DeterlabProjectEntity extends AbstractEntity implements DeterlabProject {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "ncl_team_id", nullable = false, unique = true)
    private String nclTeamId;

    @Column(name = "deter_project_id", nullable = false, unique = true)
    private String deterProjectId;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNclTeamId() {
        return nclTeamId;
    }

    public void setNclTeamId(String nclTeamId) {
        this.nclTeamId = nclTeamId;
    }

    public String getDeterProjectId() {
        return deterProjectId;
    }

    public void setDeterProjectId(String deterProjectId) {
        this.deterProjectId = deterProjectId;
    }
}
