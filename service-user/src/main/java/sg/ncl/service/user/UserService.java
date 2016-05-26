package sg.ncl.service.user;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.dtos.UserInfo;
import sg.ncl.service.user.exceptions.NoUsersFoundException;
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

        if (result.size() == 0) {
            throw new NoUsersFoundException();
        }

        return result;
    }

    public User find(final String id) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            throw new UserNotFoundException();
        }
        return one;
    }

    public void update(final String id, final UserEntity user) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            return;
        }

        final UserDetails oneUserDetails = one.getUserDetails();
        final UserDetails userUserDetails = user.getUserDetails();

        if (oneUserDetails.getFirstName().equals(userUserDetails.getFirstName())) {
            one.getUserDetails().setFirstName(userUserDetails.getFirstName());
        }

        if (oneUserDetails.getLastName().equals(userUserDetails.getLastName())) {
            one.getUserDetails().setLastName(userUserDetails.getLastName());
        }

        if (oneUserDetails.getEmail().equals(userUserDetails.getEmail())) {
            one.getUserDetails().setEmail(userUserDetails.getEmail());
        }

        if (oneUserDetails.getPhone().equals(userUserDetails.getPhone())) {
            one.getUserDetails().setPhone(userUserDetails.getPhone());
        }

        final Address oneAddress = one.getUserDetails().getAddress();
        final Address userAddress = user.getUserDetails().getAddress();

        if (oneAddress.getAddress1().equals(userAddress.getAddress1())) {
            one.getUserDetails().getAddress().setAddress1(userAddress.getAddress1());
        }

        if (oneAddress.getAddress2().equals(userAddress.getAddress2())) {
            one.getUserDetails().getAddress().setAddress2(userAddress.getAddress2());
        }

        if (oneAddress.getCountry().equals(userAddress.getCountry())) {
            one.getUserDetails().getAddress().setCountry(userAddress.getCountry());
        }

        if (oneAddress.getRegion().equals(userAddress.getRegion())) {
            one.getUserDetails().getAddress().setRegion(userAddress.getRegion());
        }

        if (oneAddress.getZipCode().equals(userAddress.getZipCode())) {
            one.getUserDetails().getAddress().setZipCode((userAddress.getZipCode()));
        }

        userRepository.save(one);
    }

    public void seedData() {

        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName("first");
        userDetailsEntity.setLastName("last");
        userDetailsEntity.setEmail("email");
        userDetailsEntity.setPhone("phone");

        final AddressEntity address = new AddressEntity();
        address.setAddress1("address1");
        address.setAddress2("address2");
        address.setCountry("country");
        address.setRegion("region");
        address.setZipCode("zipcode");

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        userRepository.save(userEntity);
    }
}
