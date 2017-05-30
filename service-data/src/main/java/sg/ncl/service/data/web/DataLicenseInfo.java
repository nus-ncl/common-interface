package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataLicense;

/**
 * Created by dcsjnh on 30/5/2017.
 */
@Getter
public class DataLicenseInfo implements DataLicense {

    private Long id;
    private String name;
    private String acronym;
    private String description;
    private String link;

    @JsonCreator
    public DataLicenseInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("acronym") final String acronym,
            @JsonProperty("description") final String description,
            @JsonProperty("link") final String link
    ) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.description = description;
        this.link = link;
    }

    public DataLicenseInfo(DataLicense dataLicense) {
        this(
                dataLicense.getId(),
                dataLicense.getName(),
                dataLicense.getAcronym(),
                dataLicense.getDescription(),
                dataLicense.getLink()
        );
    }
}
