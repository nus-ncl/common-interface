package sg.ncl.service.user;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.dtos.UserInfo;

import javax.inject.Inject;
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

    public User find(final String id) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            // TODO throw exception
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

        if (oneUserDetails.getFirstName() != userUserDetails.getFirstName()) {
            one.getUserDetails().setFirstName(userUserDetails.getFirstName());
        }

        if (oneUserDetails.getLastName() != userUserDetails.getLastName()) {
            one.getUserDetails().setLastName(userUserDetails.getLastName());
        }

        if (oneUserDetails.getEmail() != userUserDetails.getEmail()) {
            one.getUserDetails().setEmail(userUserDetails.getEmail());
        }

        if (oneUserDetails.getPhone() != userUserDetails.getPhone()) {
            one.getUserDetails().setPhone(userUserDetails.getPhone());
        }

        final Address oneAddress = one.getUserDetails().getAddress();
        final Address userAddress = user.getUserDetails().getAddress();

        if (oneAddress.getAddress1() != userAddress.getAddress1()) {
            one.getUserDetails().getAddress().setAddress1(userAddress.getAddress1());
        }

        if (oneAddress.getAddress2() != userAddress.getAddress2()) {
            one.getUserDetails().getAddress().setAddress2(userAddress.getAddress2());
        }

        if (oneAddress.getCountry() != userAddress.getCountry()) {
            one.getUserDetails().getAddress().setCountry(userAddress.getCountry());
        }

        if (oneAddress.getRegion() != userAddress.getRegion()) {
            one.getUserDetails().getAddress().setRegion(userAddress.getRegion());
        }

        if (oneAddress.getZipCode() != userAddress.getZipCode()) {
            one.getUserDetails().getAddress().setZipCode((userAddress.getZipCode()));
        }

        userRepository.save(one);
    }

}
