package sg.ncl.service.user.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.*;
import sg.ncl.service.user.util.TestUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Tran Ly Vu
 * @Version 1.0
 */

public class UserServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private AdapterDeterLab adapterDeterLab;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userServiceImpl;

    @Before
    public void setup() {
        assertThat(mockingDetails(userRepository).isMock()).isTrue();
        userServiceImpl = new UserServiceImpl(userRepository, credentialsService, adapterDeterLab);
    }

    //throw UsernameAlreadyExistsException
    @Test
    public void createUserTest1() throws Exception {
        UserEntity userEntity = TestUtil.getUserEntity();

        exception.expect(UsernameAlreadyExistsException.class);
        when(userRepository.findByUserDetailsEmail(anyString())).thenReturn(userEntity);
        userServiceImpl.createUser(userEntity);

        verify(userRepository, times(1)).findByUserDetailsEmail(anyString());
    }

    //might be wrong gotta ask christ
    //no exception thrown
    @Test
    public void createUserTest2() throws Exception {
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();

        userDetailsEntity.setEmail(null);

        User userEntity = new UserEntity();
        userEntity = TestUtil.getUserEntity();

        when(userRepository.findByUserDetailsEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn((UserEntity) userEntity);
        User actual = userServiceImpl.createUser(userEntity);

        verify(userRepository, times(1)).findByUserDetailsEmail(anyString());
        verify(userRepository, times(1)).save((any(UserEntity.class)));

        assertThat(actual.getApplicationDate()).isEqualTo(userEntity.getApplicationDate());
        assertThat(actual.getUserDetails()).isEqualTo(userEntity.getUserDetails());
        assertThat(actual.getProcessedDate()).isEqualTo(userEntity.getProcessedDate());

    }

    @Test
    public void testGetAllListWithSizeOfZero() {
        List<User> actual = userServiceImpl.getAll();
        verify(userRepository, times(1)).findAll();
        Assert.assertTrue(actual.size() == 0);
    }

    //throw UserNotFoundException
    @Test
    public void testGetUserWhenIdIsNull() {

        exception.expect(UserIdNullOrEmptyException.class);

        User actual = userServiceImpl.getUser(null);
    }

    //throw UserNotFoundException
    @Test
    public void testGetUserWhenIdIsEmpty() {
        exception.expect(UserIdNullOrEmptyException.class);

        User actual = userServiceImpl.getUser("");
    }

    //No exception thrown
    @Test
    public void testGetUserNoExceptionThrown() {
        String randomStringForTest = RandomStringUtils.randomAlphanumeric(20);

        User actual = userServiceImpl.getUser(randomStringForTest);
        verify(userRepository, times(1)).findOne(randomStringForTest);
    }

    //throw UserNotFoundException
    @Test
    public void testVerifyEmailUserNotFoundException() {
        String randomIdStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyStringForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserNotFoundException.class);
        when(userRepository.findOne(anyString())).thenReturn(null);

        userServiceImpl.verifyUserEmail(randomIdStringForTest, randomEmailStringForTest, randomKeyStringForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testVerifyEmailIdIsNull() {
        String randomEmailStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyStringForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.verifyUserEmail(null, randomEmailStringForTest, randomKeyStringForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserNotFoundException
    @Test
    public void testVerifyEmailWhenUidIsEmpty() {
        String randomEmailStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyStringForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.verifyUserEmail("", randomEmailStringForTest, randomKeyStringForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw VerificationEmailNotMatchException
    @Test
    public void testVerifyEmailEmailNotMatch() throws Exception {
        String randomUidStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailStringForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyStringForTest = RandomStringUtils.randomAlphanumeric(20);

        UserEntity userEntity = TestUtil.getUserEntity();
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();

        exception.expect(VerificationEmailNotMatchException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);

        userServiceImpl.verifyUserEmail(randomUidStringForTest, randomEmailStringForTest, randomKeyStringForTest);
    }


    //no exception thrown and user.getStatus() == UserStatus.CREATED
    @Test
    public void testVerifyEmailNoException1() throws Exception {
        String randomUidForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyForTest = RandomStringUtils.randomAlphanumeric(20);

        UserEntity userEntity = TestUtil.getUserEntity();
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        userDetailsEntity.setEmail(randomEmailForTest);               //in order to pass mailNotMatchException exception
        userEntity.setUserDetails(userDetailsEntity);
        userEntity.setVerificationKey(randomKeyForTest);                //verificationKey is not null
        userEntity.setStatus(UserStatus.CREATED);                     //user.getStatus() == UserStatus.CREATED

        UserEntity userEntity1 = userEntity;
        userEntity1.setEmailVerified(true);
        userEntity1.setStatus(UserStatus.PENDING);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity1);

        User actual = userServiceImpl.verifyUserEmail(randomUidForTest, randomEmailForTest, randomKeyForTest);

        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertThat(actual.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    //no exception thrown and user.getStatus() != UserStatus.CREATED
    @Test
    public void testVerifyEmailNoException2() throws Exception {
        String randomUidForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyForTest = RandomStringUtils.randomAlphanumeric(20);

        UserEntity userEntity = TestUtil.getUserEntity();
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        userDetailsEntity.setEmail(randomEmailForTest);               //in order to pass mailNotMatchException exception
        userEntity.setUserDetails(userDetailsEntity);
        userEntity.setVerificationKey(randomKeyForTest);                //verificationKey is not null
        userEntity.setStatus(UserStatus.PENDING);                     //user.getStatus() == UserStatus.CREATED

        UserEntity userEntity1 = userEntity;
        userEntity1.setEmailVerified(true);
        userEntity1.setStatus(UserStatus.PENDING);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity1);

        User actual = userServiceImpl.verifyUserEmail(randomUidForTest, randomEmailForTest, randomKeyForTest);

        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertThat(actual.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    //throw VerificationKeyNotMatchException when user.getVerificationKey()==null
    @Test
    public void testVerifyEmailKeyIsNull() throws Exception {
        String randomUidForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyForTest = RandomStringUtils.randomAlphanumeric(20);

        UserEntity userEntity = TestUtil.getUserEntity();
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        userDetailsEntity.setEmail(randomEmailForTest);               //in order to pass mailNotMatchException exception
        userEntity.setUserDetails(userDetailsEntity);
        userEntity.setVerificationKey(null);                //verificationKey null

        exception.expect(VerificationKeyNotMatchException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);

        userServiceImpl.verifyUserEmail(randomUidForTest, randomEmailForTest, randomKeyForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw VerificationKeyNotMatchException when !key.equals(user.getVerificationKey())
    @Test
    public void testVerifyEmailKeyIsNOtEqualVerificationKey() throws Exception {
        String randomUidForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomEmailForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomKeyForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomVerificationKeyForTest = RandomStringUtils.randomAlphanumeric(25);

        UserEntity userEntity = TestUtil.getUserEntity();
        UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        userDetailsEntity.setEmail(randomEmailForTest);               //in order to pass mailNotMatchException exception
        userEntity.setUserDetails(userDetailsEntity);
        userEntity.setVerificationKey(randomVerificationKeyForTest);             //verificationKey is not equal key

        exception.expect(VerificationKeyNotMatchException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);

        userServiceImpl.verifyUserEmail(randomUidForTest, randomEmailForTest, randomKeyForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserNotFoundException
    @Test
    public void testUpdateUserUserNotFoundException() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();

        when(userRepository.findOne(anyString())).thenReturn(null);
        exception.expect(UserNotFoundException.class);

        userServiceImpl.updateUser(randomIdForTest, (User) userEntity);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testUpdateUserIdIsNull() throws Exception {
        UserEntity userEntity = TestUtil.getUserEntity();

        exception.expect(UserIdNullOrEmptyException.class);

        userServiceImpl.updateUser(null, (User) userEntity);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testUpdateUserIdIsEmpty() throws Exception {
        UserEntity userEntity = TestUtil.getUserEntity();

        exception.expect(UserIdNullOrEmptyException.class);

        userServiceImpl.updateUser("", (User) userEntity);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //no exception thrown
    @Test
    public void testUpdateUserNoException() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setId(randomIdForTest);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userServiceImpl.updateUser(randomIdForTest, (User) userEntity);

        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    //thrown UserNotFoundException
    @Test
    public void testAddTeamUserNotFoundException() throws Exception {
        String randomUserIdForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        when(userRepository.findOne(anyString())).thenReturn(null);
        exception.expect(UserNotFoundException.class);

        userServiceImpl.addTeam(randomUserIdForTest, randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());

    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testAddTeamIdIsNull() {
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.addTeam(null, randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testAddTeamIdIsEmpty() {
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.addTeam("", randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //no exception thrown
    @Test
    public void testAddTeamNoException() throws Exception {
        String randomUserIdForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        userServiceImpl.addTeam(randomUserIdForTest, randomTeamIdForTest);

        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testRemoveTeamIdIsNull() {
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.removeTeam(null, randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testRemoveTeamIdIsEmpty() {
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        userServiceImpl.removeTeam("", randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //thrown UserNotFoundException
    @Test
    public void testRemoveTeamUserNotFoundException() throws Exception {
        String randomUserIdForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);

        when(userRepository.findOne(anyString())).thenReturn(null);
        exception.expect(UserNotFoundException.class);

        userServiceImpl.removeTeam(randomUserIdForTest, randomTeamIdForTest);

        verify(userRepository, times(1)).findOne(anyString());

    }

    //no Exception thrown
    @Test
    public void testRemoveTeamNoException() throws Exception {
        String randomUserIdForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        userServiceImpl.addTeam(randomUserIdForTest, randomTeamIdForTest);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    //throw UserNotFoundException
    @Test
    public void testUpdateUserStatusUserNotFoundException() {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);

        when(userRepository.findOne(anyString())).thenReturn(null);
        exception.expect(UserNotFoundException.class);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.APPROVED);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testUpdateUserStatusIdIsNull() {
        exception.expect(UserIdNullOrEmptyException.class);
        User actual = userServiceImpl.updateUserStatus(null, UserStatus.APPROVED);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw UserIdNullOrEmptyException
    @Test
    public void testUpdateUserStatusIdIsEmpty() {
        exception.expect(UserIdNullOrEmptyException.class);
        User actual = userServiceImpl.updateUserStatus("", UserStatus.APPROVED);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCaseCreated() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity= TestUtil.getUserEntity();

        exception.expect(InvalidStatusTransitionException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.CREATED);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //case PENDING- If branch
    @Test
    public void testUpdateUserStatusCasePendingIfBranch() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.CREATED);
        User expected = new UserEntity();
        expected = userEntity;

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.PENDING);


        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    //case PENDING- else branch , throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCasePendingElseBranch() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.PENDING);

        exception.expect(InvalidStatusTransitionException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.PENDING);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //case APPROVED- If branch
    @Test
    public void testUpdateUserStatusCaseApprove() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.PENDING);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.APPROVED);
        User expected = new UserEntity();
        expected = userEntity;

        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    //case APPROVED- else branch, throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCaseApprovedElseBranch() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.APPROVED);

        exception.expect(InvalidStatusTransitionException.class);
        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.APPROVED);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //case CLOSED- else branch, throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCaseClosed() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.CLOSED);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.CLOSED);
        User expected = new UserEntity();
        expected = userEntity;

        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    //case FROZEN
    @Test
    public void testUpdateUserStatusCaseFrozen() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.APPROVED);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.FROZEN);
        User expected = userEntity;

        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // case FROZEN - else branch, throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCaseFrozenElseBranch() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.CLOSED);

        exception.expect(InvalidStatusTransitionException.class);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.FROZEN);

        verify(userRepository, times(1)).findOne(anyString());
    }

    //case FROZEN to APPROVED
    // should not throw InvalidStatusTransitionException
    @Test
    public void testUpdateUserStatusCaseFrozenToApproved() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.FROZEN);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User actual = userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.APPROVED);
        User expected = userEntity;

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getStatus()).isEqualTo(UserStatus.APPROVED);
        verify(userRepository, times(1)).findOne(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testRemoveUserWhereUserIsNotFound() {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);

        when(userRepository.findOne(anyString())).thenReturn(null);

        exception.expect(UserNotFoundException.class);
        userServiceImpl.removeUser(randomIdForTest);
    }

    @Test
    public void testRemoveUserWhereUserHasTeam() throws Exception {
        String randomUserIdForTest = RandomStringUtils.randomAlphanumeric(20);
        String randomTeamIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        userServiceImpl.addTeam(randomUserIdForTest, randomTeamIdForTest);

        exception.expect(UserIsNotDeletableException.class);
        userServiceImpl.removeUser(randomUserIdForTest);
    }

    @Test
    public void testRemoveUserWhereUserStatusNotCreatedOrPending() throws Exception {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = TestUtil.getUserEntity();
        userEntity.setStatus(UserStatus.PENDING);

        when(userRepository.findOne(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        userServiceImpl.updateUserStatus(randomIdForTest, UserStatus.APPROVED);

        exception.expect(UserIsNotDeletableException.class);
        userServiceImpl.removeUser(randomIdForTest);
    }
}
