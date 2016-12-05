package sg.ncl.service.transmission.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.transmission.domain.Resumable;

/**
 * Created by dcsjnh on 11/24/2016.
 */
@Getter
public class ResumableInfo implements Resumable {

    private Integer  resumableChunkSize;
    private Long     resumableTotalSize;
    private String   resumableIdentifier;
    private String   resumableFilename;
    private String   resumableRelativePath;
    private String   resumableChunk;

    @JsonCreator
    public ResumableInfo(
            @JsonProperty("resumableChunkSize") final Integer resumableChunkSize,
            @JsonProperty("resumableTotalSize") final Long resumableTotalSize,
            @JsonProperty("resumableIdentifier") final String resumableIdentifier,
            @JsonProperty("resumableFilename") final String resumableFilename,
            @JsonProperty("resumableRelativePath") final String resumableRelativePath,
            @JsonProperty("resumableChunk") final String resumableChunk
    ) {
        this.resumableChunkSize = resumableChunkSize;
        this.resumableTotalSize = resumableTotalSize;
        this.resumableIdentifier = resumableIdentifier;
        this.resumableFilename = resumableFilename;
        this.resumableRelativePath = resumableRelativePath;
        this.resumableChunk = resumableChunk;
    }

    public ResumableInfo(Resumable resumable) {
        this(
                resumable.getResumableChunkSize(),
                resumable.getResumableTotalSize(),
                resumable.getResumableIdentifier(),
                resumable.getResumableFilename(),
                resumable.getResumableRelativePath(),
                null
        );
    }

}
