package sg.ncl.service.user.web;

import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.domain.UserDetails;

/**
 * @author Christopher Zhong
 */
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

    public UserDetailsInfo(final String firstName, final String lastName, final String jobTitle, final AddressInfo address, final String email, final String phone, final String institution, final String institutionAbbreviation, final String institutionWeb) {
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

    public UserDetailsInfo(UserDetailsEntity userDetailsEntity) {
        this(userDetailsEntity.getFirstName(),
                userDetailsEntity.getLastName(),
                userDetailsEntity.getJobTitle(),
                new AddressInfo(userDetailsEntity.getAddress()),
                userDetailsEntity.getEmail(),
                userDetailsEntity.getPhone(),
                userDetailsEntity.getInstitution(),
                userDetailsEntity.getInstitutionAbbreviation(),
                userDetailsEntity.getInstitutionWeb()
        );
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public AddressInfo getAddress() {
        return address;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }


    @Override
    public String getInstitution() {
        return institution;
    }

    @Override
    public String getInstitutionAbbreviation() {
        return institutionAbbreviation;
    }

    @Override
    public String getInstitutionWeb() {
        return institutionWeb;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }
}
