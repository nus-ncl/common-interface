package sg.ncl.testbed_interface.repositories.jpa.entities;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.testbed_interface.domain.Team;
import sg.ncl.testbed_interface.domain.TeamStatus;
import sg.ncl.testbed_interface.domain.TeamVisibility;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "teams")
public class TeamEntity extends AbstractEntity implements Team {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id = null;

    @Column(name = "name", nullable = false, unique = true)
    private String name = null;

    @Column(name = "description")
    private String description = null;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamVisibility visibility = TeamVisibility.PUBLIC;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamStatus status = TeamStatus.PENDING;

    @Column(name = "application_date", nullable = false)
    private ZonedDateTime applicationDate = null;

    @Column(name = "processed_date")
    private ZonedDateTime processedDate = null;

    @ManyToMany
    @ElementCollection
    private final ConcurrentMap<String, UserEntity> members = new ConcurrentHashMap<>();

    @Override
    public String getId() {
        return id;
    }

    void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public TeamVisibility getVisibility() {
        return visibility;
    }

    void setVisibility(final TeamVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public TeamStatus getStatus() {
        return status;
    }

    void setStatus(final TeamStatus status) {
        this.status = status;
    }

    @Override
    public ZonedDateTime getApplicationDate() {
        return this.applicationDate;
    }

    void setApplicationDate(ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public ZonedDateTime getProcessedDate() {
        return this.processedDate;
    }

    void setProcessedDate(ZonedDateTime processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public List<UserEntity> getMembers() {
        return new ArrayList<>(members.values());
    }
}
