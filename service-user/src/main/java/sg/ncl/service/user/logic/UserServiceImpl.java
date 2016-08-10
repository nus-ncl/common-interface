package sg.ncl.service.user.logic;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christopher Zhong
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(user.getApplicationDate());
        userEntity.setProcessedDate(user.getProcessedDate());
        userEntity.setUserDetails((UserDetailsEntity) user.getDetails());

        UserEntity savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity;
    }

    @Transactional
    public List<User> getAll() {
        return userRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    public User getUser(final String id) {
        return findUser(id);
    }

    @Transactional
    public void updateUser(final String id, final User user) {
        final UserEntity one = findUser(id);

        if (user.getDetails().getFirstName() != null) {
            one.getDetails().setFirstName(user.getDetails().getFirstName());
        }

        if (user.getDetails().getLastName() != null) {
            one.getDetails().setLastName(user.getDetails().getLastName());
        }

        if (user.getDetails().getJobTitle() != null) {
            one.getDetails().setJobTitle(user.getDetails().getJobTitle());
        }

        if (user.getDetails().getEmail() != null) {
            one.getDetails().setEmail(user.getDetails().getEmail());
        }

        if (user.getDetails().getPhone() != null) {
            one.getDetails().setPhone(user.getDetails().getPhone());
        }

        if (user.getDetails().getInstitution() != null) {
            one.getDetails().setInstitution(user.getDetails().getInstitution());
        }

        if (user.getDetails().getInstitutionAbbreviation() != null) {
            one.getDetails().setInstitutionAbbreviation(user.getDetails().getInstitutionAbbreviation());
        }

        if (user.getDetails().getInstitutionWeb() != null) {
            one.getDetails().setInstitutionWeb(user.getDetails().getInstitutionWeb());
        }

        final Address userAddress = user.getDetails().getAddress();

        if (userAddress != null) {

            if (userAddress.getAddress1() != null) {
                one.getDetails().getAddress().setAddress1(userAddress.getAddress1());
            }

            if (userAddress.getAddress2() != null) {
                one.getDetails().getAddress().setAddress2(userAddress.getAddress2());
            }

            if (userAddress.getCountry() != null) {
                one.getDetails().getAddress().setCountry(userAddress.getCountry());
            }

            if (userAddress.getCity() != null) {
                one.getDetails().getAddress().setCity(userAddress.getCity());
            }

            if (userAddress.getRegion() != null) {
                one.getDetails().getAddress().setRegion(userAddress.getRegion());
            }

            if (userAddress.getZipCode() != null) {
                one.getDetails().getAddress().setZipCode((userAddress.getZipCode()));
            }
        }

        userRepository.save(one);
    }

    @Transactional
    public void addTeam(final String userId, final String teamId) {
        UserEntity one = findUser(userId);
        one.addTeamId(teamId);
        userRepository.save(one);
    }

    @Transactional
    public void removeTeam(final String userId, final String teamId) {
        UserEntity one = findUser(userId);
        one.removeTeamId(teamId);
        userRepository.save(one);
    }

    private UserEntity findUser(final String userId) {
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
