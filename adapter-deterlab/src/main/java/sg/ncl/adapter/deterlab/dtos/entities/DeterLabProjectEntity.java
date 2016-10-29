package sg.ncl.adapter.deterlab.dtos.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import sg.ncl.adapter.deterlab.domain.DeterLabProject;
import sg.ncl.common.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Te Ye on 16-Jun-16.
 */
@Entity
@Table(name = "deterlab_project")
@Getter
@Setter
public class DeterLabProjectEntity extends AbstractEntity implements DeterLabProject {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "ncl_team_id", nullable = false, unique = true)
    private String nclTeamId;

    @Column(name = "deter_project_id", nullable = false, unique = true)
    private String deterProjectId;
}
