package sg.ncl.service.user.logic;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;
import sg.ncl.service.user.web.UserInfo;

import javax.inject.Inject;
import javax.transaction.Transactional;
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
    public User createUser(User user) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(user.getApplicationDate());
        userEntity.setProcessedDate(user.getProcessedDate());
        userEntity.setUserDetails((UserDetailsEntity) user.getUserDetails());

        UserEntity savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity;
    }

    @Transactional
    public List<User> getAll() {
        final List<User> result = new ArrayList<>();
        for (UserEntity user : userRepository.findAll()) {
            result.add(user);
        }

        return result;
    }

    @Transactional
    public User findUser(final String id) {
        return new UserInfo(findUserEntity(id));
    }

    @Transactional
    public void updateUser(final String id, final User user) {
        final UserEntity one = findUserEntity(id);

        final UserDetails user2 = user.getUserDetails();

        if (user.getUserDetails().getFirstName() != null) {
            one.getUserDetails().setFirstName(user.getUserDetails().getFirstName());
        }

        if (user.getUserDetails().getLastName() != null) {
            one.getUserDetails().setLastName(user.getUserDetails().getLastName());
        }

        if (user.getUserDetails().getJobTitle() != null) {
            one.getUserDetails().setJobTitle(user.getUserDetails().getJobTitle());
        }

        if (user.getUserDetails().getEmail() != null) {
            one.getUserDetails().setEmail(user.getUserDetails().getEmail());
        }

        if (user.getUserDetails().getPhone() != null) {
            one.getUserDetails().setPhone(user.getUserDetails().getPhone());
        }

        if (user.getUserDetails().getInstitution() != null) {
            one.getUserDetails().setInstitution(user.getUserDetails().getInstitution());
        }

        if (user.getUserDetails().getInstitutionAbbreviation() != null) {
            one.getUserDetails().setInstitutionAbbreviation(user.getUserDetails().getInstitutionAbbreviation());
        }

        if (user.getUserDetails().getInstitutionWeb() != null) {
            one.getUserDetails().setInstitutionWeb(user.getUserDetails().getInstitutionWeb());
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

    @Transactional
    public void addTeam(final String userId, final String teamId) {
        UserEntity one = findUserEntity(userId);
        one.addTeamId(teamId);
        userRepository.save(one);
    }

    private UserEntity findUserEntity(final String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UserIdNullException();
        }

        final UserEntity one = userRepository.findOne(userId);
        if (one == null) {
            throw new UserNotFoundException();
        }
        return one;
    }
}
