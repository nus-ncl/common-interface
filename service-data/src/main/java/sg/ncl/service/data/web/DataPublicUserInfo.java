package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataPublicUser;

@Getter
public class DataPublicUserInfo implements DataPublicUser {

    private Long id;
    private String fullName;
    private String email;
    private String jobTitle;
    private String institution;
    private String country;
    private boolean licenseAgreed;

    @JsonCreator
    public DataPublicUserInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("fullName") final String fullName,
            @JsonProperty("email") final String email,
            @JsonProperty("jobTitle") final String jobTitle,
            @JsonProperty("institution") final String institution,
            @JsonProperty("country") final String country,
            @JsonProperty("licenseAgreed") final boolean licenseAgreed
    ) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.jobTitle = jobTitle;
        this.institution = institution;
        this.country = country;
        this.licenseAgreed = licenseAgreed;
    }

    public DataPublicUserInfo(DataPublicUser dataPublicUser) {
        this(
                dataPublicUser.getId(),
                dataPublicUser.getFullName(),
                dataPublicUser.getEmail(),
                dataPublicUser.getJobTitle(),
                dataPublicUser.getInstitution(),
                dataPublicUser.getCountry(),
                true
        );
    }

}
