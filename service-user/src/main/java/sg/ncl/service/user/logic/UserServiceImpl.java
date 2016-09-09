package sg.ncl.service.user.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.InvalidStatusTransitionException;
import sg.ncl.service.user.exceptions.InvalidUserStatusException;
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;
import sg.ncl.service.user.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.findByUserDetailsEmail(user.getUserDetails().getEmail()) != null) {
            log.warn("Username '{}' is already associated with a credentials", user.getUserDetails().getEmail());
            throw new UsernameAlreadyExistsException(user.getUserDetails().getEmail());
        }
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(user.getApplicationDate());
        userEntity.setProcessedDate(user.getProcessedDate());
        userEntity.setUserDetails((UserDetailsEntity) user.getUserDetails());
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
        UserEntity one = findUser(userId);
        one.addTeam(teamId);
        userRepository.save(one);
    }

    @Transactional
    public void removeTeam(final String userId, final String teamId) {
        UserEntity one = findUser(userId);
        one.removeTeam(teamId);
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

    @Override
    @Transactional
    public User updateUserStatus(final String id, final UserStatus status) {
        UserEntity one = findUser(id);
        switch(status) {
            case CREATED:
                throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
            case PENDING:
                if(one.getStatus().equals(UserStatus.CREATED)) {
                    return updateUserStatusInternal(one, UserStatus.PENDING);
                } else {
                    throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
                }
            case APPROVED:
                if(one.getStatus().equals(UserStatus.PENDING)) {
                    return updateUserStatusInternal(one, UserStatus.APPROVED);
                } else {
                    throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
                }
            case REJECTED:
                if(one.getStatus().equals(UserStatus.PENDING)) {
                    return updateUserStatusInternal(one, UserStatus.REJECTED);
                } else {
                    throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
                }
            case CLOSED:
                return updateUserStatusInternal(one, UserStatus.CLOSED);
            default:
                log.warn("Update status failed for {}: unknown status {}", id, status);
                throw new InvalidUserStatusException(status.toString());
        }
    }

    @Transactional
    private User updateUserStatusInternal(UserEntity user, UserStatus status) {
        UserStatus oldStatus = user.getStatus();
        user.setStatus(status);
        UserEntity savedUser = userRepository.save(user);
        log.info("Status updated for {}: {} -> {}", user.getId(), oldStatus, savedUser.getStatus());
        return savedUser;
    }
}
