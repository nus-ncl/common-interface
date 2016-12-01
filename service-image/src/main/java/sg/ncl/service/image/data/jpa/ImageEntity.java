package sg.ncl.service.image.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageVisibility;

import javax.persistence.*;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
@Entity
@Table(name = "images")
@Getter
@Setter
public class ImageEntity extends AbstractEntity implements Image {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "team_id", nullable = false, updatable = false)
    private String teamId;

    @Column(name = "node_id", nullable = false, updatable = false)
    private String nodeId;

    @Column(name = "image_name", nullable = false, updatable = false)
    private String imageName;

    @Column(name = "description", nullable = true, updatable = false)
    private String description;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageVisibility visibility = ImageVisibility.PRIVATE;

    @Column(name = "current_os", nullable = false)
    private String currentOS;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Image that = (Image) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "id='" + id + '\'' +
                ", teamId='" + teamId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", imageName=" + imageName + '\'' +
                ", description=" + description + '\'' +
                ", visibility=" + visibility + '\'' +
                ", currentOS=" + currentOS +
                "} " + super.toString();
    }
}
