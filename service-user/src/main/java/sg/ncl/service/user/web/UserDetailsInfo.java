package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sg.ncl.service.user.domain.UserDetails;

/**
 * @author Christopher Zhong
 */
@Getter
public class UserDetailsInfo implements UserDetails {

    private final String firstName;
    private final String lastName;
    private final String jobTitle;
    private final AddressInfo address;
    private final String email;
    private final String phone;
    private final String institution;
    private final String institutionAbbreviation;
    private final String institutionWeb;

    @JsonCreator
    public UserDetailsInfo(
            @JsonProperty("firstName") final String firstName,
            @JsonProperty("lastName") final String lastName,
            @JsonProperty("jobTitle") final String jobTitle,
            @JsonProperty("address") final AddressInfo address,
            @JsonProperty("email") final String email,
            @JsonProperty("phone") final String phone,
            @JsonProperty("institution") final String institution,
            @JsonProperty("institutionAbbreviation") final String institutionAbbreviation,
            @JsonProperty("institutionWeb") final String institutionWeb
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.institution = institution;
        this.institutionAbbreviation = institutionAbbreviation;
        this.institutionWeb = institutionWeb;
    }

    public UserDetailsInfo(UserDetails userDetails) {
        this(
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getJobTitle(),
                new AddressInfo(userDetails.getAddress()),
                userDetails.getEmail(),
                userDetails.getPhone(),
                userDetails.getInstitution(),
                userDetails.getInstitutionAbbreviation(),
                userDetails.getInstitutionWeb()
        );
    }


}
