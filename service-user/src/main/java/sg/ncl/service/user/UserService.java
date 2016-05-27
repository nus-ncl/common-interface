package sg.ncl.service.user;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Inject
    protected UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> get() {
        final List<User> result = new ArrayList<>();
        for (UserEntity user : userRepository.findAll()) {
            result.add(user);
        }
        return result;
    }

    public UserEntity find(final String id) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            throw new UserNotFoundException();
        }
        return one;
    }

    public void update(final String id, final UserEntity user) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            throw new UserNotFoundException();
        }

        final UserDetails userUserDetails = user.getUserDetails();

        if (userUserDetails.getFirstName() != null) {
            one.getUserDetails().setFirstName(userUserDetails.getFirstName());
        }

        if (userUserDetails.getLastName() != null) {
            one.getUserDetails().setLastName(userUserDetails.getLastName());
        }

        if (userUserDetails.getEmail() != null) {
            one.getUserDetails().setEmail(userUserDetails.getEmail());
        }

        if (userUserDetails.getPhone() != null) {
            one.getUserDetails().setPhone(userUserDetails.getPhone());
        }

        final Address userAddress = user.getUserDetails().getAddress();

        if (userAddress != null) {

            if (userAddress.getAddress1() != null) {
                one.getUserDetails().getAddress().setAddress1(userAddress.getAddress1());
            }

            if (userAddress.getAddress2() != null) {
                one.getUserDetails().getAddress().setAddress2(userAddress.getAddress2());
            }

            if (userAddress.getCountry() != null) {
                one.getUserDetails().getAddress().setCountry(userAddress.getCountry());
            }

            if (userAddress.getRegion() != null) {
                one.getUserDetails().getAddress().setRegion(userAddress.getRegion());
            }

            if (userAddress.getZipCode() != null) {
                one.getUserDetails().getAddress().setZipCode((userAddress.getZipCode()));
            }
        }

        userRepository.save(one);
    }

    public void seedData() {

        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());
        userEntity.setCreatedDate(ZonedDateTime.now());
        userEntity.setLastModifiedDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setCreatedDate(ZonedDateTime.now());
        userDetailsEntity.setLastModifiedDate(ZonedDateTime.now());
        userDetailsEntity.setFirstName("john");
        userDetailsEntity.setLastName("doe");
        userDetailsEntity.setEmail("johndoe@nus.edu.sg");
        userDetailsEntity.setPhone("12345678");

        final AddressEntity address = new AddressEntity();
        address.setCreatedDate(ZonedDateTime.now());
        address.setLastModifiedDate(ZonedDateTime.now());
        address.setAddress1("computing drive 12");
        address.setAddress2("another address 2");
        address.setCountry("singapore");
        address.setRegion("west");
        address.setZipCode("123456");

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        userRepository.save(userEntity);
    }
}
