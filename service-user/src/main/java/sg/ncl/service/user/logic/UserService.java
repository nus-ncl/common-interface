package sg.ncl.service.user.logic;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import javax.transaction.Transactional;
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

    @Transactional
    public String addUser(User user) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(user.getApplicationDate());
        userEntity.setProcessedDate(user.getProcessedDate());
        userEntity.setUserDetails((UserDetailsEntity) user.getUserDetails());

        UserEntity savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity.getId();
    }

    @Transactional
    public List<User> get() {
        final List<User> result = new ArrayList<>();
        for (UserEntity user : userRepository.findAll()) {
            result.add(user);
        }

        return result;
    }

    @Transactional
    public UserEntity find(final String id) {
        if (id == null || id.isEmpty()) {
            throw new UserIdNullException();
        }

        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            throw new UserNotFoundException();
        }
        return one;
    }

    @Transactional
    public void update(final String id, final UserDetails user) {
        if (id == null || id.isEmpty()) {
            throw new UserIdNullException();
        }

        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            throw new UserNotFoundException();
        }

//        final UserDetails user = user.getUserDetails();

        if (user.getFirstName() != null) {
            one.getUserDetails().setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            one.getUserDetails().setLastName(user.getLastName());
        }

        if (user.getJobTitle() != null) {
            one.getUserDetails().setJobTitle(user.getJobTitle());
        }

        if (user.getEmail() != null) {
            one.getUserDetails().setEmail(user.getEmail());
        }

        if (user.getPhone() != null) {
            one.getUserDetails().setPhone(user.getPhone());
        }

        if (user.getInstitution() != null) {
            one.getUserDetails().setInstitution(user.getInstitution());
        }

        if (user.getInstitutionAbbreviation() != null) {
            one.getUserDetails().setInstitutionAbbreviation(user.getInstitutionAbbreviation());
        }

        if (user.getInstitutionWeb() != null) {
            one.getUserDetails().setInstitutionWeb(user.getInstitutionWeb());
        }

        final Address userAddress = user.getAddress();

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

            if (userAddress.getCity() != null) {
                one.getUserDetails().getAddress().setCity(userAddress.getCity());
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
        userDetailsEntity.setJobTitle("research assistant");
        userDetailsEntity.setEmail("johndoe@nus.edu.sg");
        userDetailsEntity.setPhone("12345678");
        userDetailsEntity.setInstitution("national university of singapore");
        userDetailsEntity.setInstitutionAbbreviation("NUS");
        userDetailsEntity.setInstitutionWeb("http://www.nus.edu.sg");

        final AddressEntity address = new AddressEntity();
        address.setCreatedDate(ZonedDateTime.now());
        address.setLastModifiedDate(ZonedDateTime.now());
        address.setAddress1("computing drive 12");
        address.setAddress2("another address 2");
        address.setCountry("singapore");
        address.setCity("singapore");
        address.setRegion("west");
        address.setZipCode("123456");

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        userRepository.save(userEntity);
    }

    @Transactional
    public void addUserToTeam(final String userId, final String teamId) {

        UserEntity userEntity = find(userId);
        userEntity.addTeamId(teamId);
        userRepository.save(userEntity);
    }
}
