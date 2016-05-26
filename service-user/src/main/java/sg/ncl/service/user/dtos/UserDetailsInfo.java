package sg.ncl.service.user.dtos;

import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.domain.UserDetails;

/**
 * @author Christopher Zhong
 */
public class UserDetailsInfo implements UserDetails {

    private final String firstName;
    private final String lastName;
    private final AddressInfo address;
    private final String email;
    private final String phone;

    public UserDetailsInfo(final String firstName, final String lastName, final AddressInfo address, final String email, final String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public UserDetailsInfo(UserDetailsEntity userDetailsEntity) {
        this(userDetailsEntity.getFirstName(),
                userDetailsEntity.getLastName(),
                new AddressInfo(userDetailsEntity.getAddress()),
                userDetailsEntity.getEmail(),
                userDetailsEntity.getPhone()
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


}
