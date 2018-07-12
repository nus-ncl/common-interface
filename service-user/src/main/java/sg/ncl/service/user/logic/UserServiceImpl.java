package sg.ncl.service.user.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CredentialsService credentialsService;
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    UserServiceImpl(final UserRepository userRepository,
                    final CredentialsService credentialsService,
                    final AdapterDeterLab adapterDeterLab) {
        this.userRepository = userRepository;
        this.credentialsService = credentialsService;
        this.adapterDeterLab = adapterDeterLab;
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
        userEntity.setVerificationKey(RandomStringUtils.randomAlphanumeric(20));
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
    @Override
    public User verifyUserEmail(@NotNull String uid, @NotNull String email, @NotNull String key) {

        log.info("Verifying email {} for user {}", email, uid);

        final UserEntity user = findUser(uid);
        if (user == null) {
            log.warn("User not found when verify email: {}", uid);
            throw new UserNotFoundException(uid);
        }

        if (!email.equals(user.getUserDetails().getEmail())) {
            log.warn("Email not match. Expected: {}, received: {}", user.getUserDetails().getEmail(), email);
            throw new VerificationEmailNotMatchException("expected: " + user.getUserDetails().getEmail() +
                    ", received: " + email);
        }

        if (user.getVerificationKey() == null || !key.equals(user.getVerificationKey())) {
            log.warn("Verification key mismatch. Expected: {}, received: {}", user.getVerificationKey(), key);
            throw new VerificationKeyNotMatchException("expected: " + user.getVerificationKey() +
                    ", received: " + key);
        }

        user.setEmailVerified(true);
        if (user.getStatus() == UserStatus.CREATED) {
            user.setStatus(UserStatus.PENDING);
        }
        final User verifiedUser = userRepository.save(user);
        log.info("User email {} has been verified.", verifiedUser.getUserDetails().getEmail());

        return verifiedUser;

    }

    @Transactional
    public User updateUser(final String id, final User user) {
        final UserEntity one = findUser(id);
        if (one == null) {
            log.warn("User not found when updating: {}", id);
            throw new UserNotFoundException(id);
        }

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
            updateAddress(one, userAddress);
        }

        final User saved = userRepository.save(one);
        log.info("User details updated: {}", saved.getUserDetails());
        return saved;
    }

    private void updateAddress(UserEntity one, Address userAddress) {
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

    @Transactional
    public void addTeam(final String userId, final String teamId) {
        UserEntity one = findUser(userId);
        if (one == null) {
            log.warn("User not found when adding team: {}", userId);
            throw new UserNotFoundException(userId);
        }
        one.addTeam(teamId);
        userRepository.save(one);
    }

    @Transactional
    public void removeTeam(final String userId, final String teamId) {
        UserEntity one = findUser(userId);
        if (one == null) {
            log.warn("User not found when removing team: {}", userId);
            throw new UserNotFoundException(userId);
        }
        one.removeTeam(teamId);
        userRepository.save(one);
    }

    private UserEntity findUser(final String id) {
        if (id == null || id.isEmpty()) {
            throw new UserIdNullOrEmptyException();
        }
        return userRepository.findOne(id);
    }

    @Override
    @Transactional
    public User updateUserStatus(final String id, final UserStatus status) {
        UserEntity one = findUser(id);
        if (one == null) {
            log.warn("User not found when update status: {}", id);
            throw new UserNotFoundException(id);
        }
        switch (status) {
            case CREATED:
                throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
            case PENDING:
                if (one.getStatus().equals(UserStatus.CREATED)) {
                    return updateUserStatusInternal(one, UserStatus.PENDING);
                } else {
                    throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
                }
            case APPROVED:
                if (one.getStatus().equals(UserStatus.PENDING) || one.getStatus().equals(UserStatus.FROZEN)) {
                    return updateUserStatusInternal(one, UserStatus.APPROVED);
                } else {
                    throw new InvalidStatusTransitionException(one.getStatus() + " -> " + status);
                }
            case FROZEN:
                if (one.getStatus().equals(UserStatus.APPROVED)) {
                    return updateUserStatusInternal(one, UserStatus.FROZEN);
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
    public User updateUserStatusInternal(UserEntity user, UserStatus status) {
        UserStatus oldStatus = user.getStatus();
        user.setStatus(status);
        UserEntity savedUser = userRepository.save(user);
        log.info("Status updated for {}: {} -> {}", user.getId(), oldStatus, savedUser.getStatus());
        return savedUser;
    }

    @Transactional
    public User removeUser(@NotNull final String id) {
        UserEntity one = findUser(id);
        if (one == null) {
            log.warn("User not found when deleting: {}", id);
            throw new UserNotFoundException(id);
        }

        List<String> teams = new ArrayList<>(one.getTeams());
        UserStatus userStatus = one.getStatus();
        if (!(teams.isEmpty() && (userStatus == UserStatus.CREATED || userStatus == UserStatus.PENDING))) {
            log.warn("User is not deletable: {}", id);
            throw new UserIsNotDeletableException(id);
        }

        // clear roles for user
        credentialsService.removeCredentials(id);

        userRepository.delete(id);
        return one;
    }

    @Override
    public String getPublicKeys(final String userId) {
        return adapterDeterLab.getPublicKeys(userId);
    }

    @Override
    public String addPublicKey(final String publicKey, final String password, final String userId) {
        if (!credentialsService.verifyPassword(userId, password)) {
            log.warn("Verification password mismatch for user {}.", userId);
            throw new VerificationPasswordNotMatchException("Verification password is invalid");
        }

        Map<String, String> errors;
        String feedback = adapterDeterLab.addPublicKey(userId, publicKey, password);
        ObjectMapper mapper = new ObjectMapper();
        try {
            errors = mapper.readValue(feedback, new TypeReference<Map<String, String>>() {});
            Optional<Map.Entry<String, String>> opt = errors.entrySet().stream().findAny();
            if (opt.isPresent()) {
                Map.Entry<String, String> entry = opt.get();
                switch (entry.getKey()) {
                    case "Pubkey Format":
                        throw new InvalidPublicKeyFormatException(entry.getValue());
                    case "PubKey File":
                        throw new InvalidPublicKeyFileException(entry.getValue());
                    case "Password":
                        throw new VerificationPasswordNotMatchException(entry.getValue());
                    default:
                        throw new BadRequestException(entry.getValue());
                }
            }
        } catch (IOException ioe) {
            throw new WebServiceException();
        }

        return feedback;
    }

    @Override
    public String deletePublicKey(final String keyId, final String userId) {
        return adapterDeterLab.deletePublicKey(userId, keyId);
    }
}
