package sg.ncl.service.image.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.image.domain.Image;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
@Getter
public class ImageInfo implements Image {

    private Long id;
    private String teamId;
    private String imageName;
    private String nodeId;
    private String description;

    @JsonCreator
    public ImageInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("teamId") final String teamId,
            @JsonProperty("imageName") final String imageName,
            @JsonProperty("nodeId") final String nodeId,
            @JsonProperty("description") final String description
    ) {
        this.id = id;
        this.teamId = teamId;
        this.imageName = imageName;
        this.nodeId = nodeId;
        this.description = description;
    }

    public ImageInfo(final Image image) {
        this(image.getId(), image.getTeamId(), image.getImageName(), image.getNodeId(), image.getDescription());
    }

}
