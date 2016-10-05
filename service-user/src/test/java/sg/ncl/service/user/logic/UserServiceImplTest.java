package sg.ncl.service.user.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.Address;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.InvalidStatusTransitionException;
import sg.ncl.service.user.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.user.exceptions.UserNotFoundException;
import sg.ncl.service.user.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static sg.ncl.service.user.Util.getUserEntity;

/**
 * @author Tran Ly Vu
 */
public class UserServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private UserDetails userDetails;

    private  UserService userServiceImpl;

    @Before
    public void  setup(){
        userServiceImpl =new UserServiceImpl(userRepository);
    }

    //throw UsernameAlreadyExistsException
    @Test
    public void createUserTest1() throws Exception {
        UserEntity userEntity= new UserEntity();
        userEntity=Util.getUserEntity();

        exception.expect(UsernameAlreadyExistsException.class);
        when(user.getUserDetails()).thenReturn(userDetails);
        when(userDetails.getEmail()).thenReturn("test");

        when(userRepository.findByUserDetailsEmail(anyString())).thenReturn(userEntity);

        User actual=userServiceImpl.createUser(user);
    }

    //no exception thrown
    @Test
    public void createUserTest2() throws Exception {
        UserEntity userEntity= new UserEntity();
        userEntity=Util.getUserEntity();

        when(user.getUserDetails()).thenReturn(userDetails);
        when(userDetails.getEmail()).thenReturn("test");

        when(userRepository.findByUserDetailsEmail(anyString())).thenReturn(null);

        User actual=userServiceImpl.createUser(user);

        assertEquals(user, actual);
    }


    @Test
    public void getAllUserWithNoUserInDbTest() {
        UserService userService = new UserServiceImpl(userRepository);
        List<User> list = userService.getAll();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void getAllUserTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        User[] userArray = new User[3];

        for (int i = 0; i < 3; i++) {
            UserEntity[] userEntityArray = addUser();
            userArray[i] = userEntityArray[0];
        }

        List<User> userList2 = userService.getAll();

        Assert.assertThat(userList2, IsIterableContainingInAnyOrder.containsInAnyOrder(userArray));
    }

    @Test(expected = UserIdNullOrEmptyException.class)
    public void getUserWithNullIdTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        userService.getUser(null);
    }

    @Test(expected = UserIdNullOrEmptyException.class)
    public void getUserWithEmptyIdTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        userService.getUser("");
    }

    @Test
    public void findUserWithNoUserInDbTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        User one = userService.getUser(RandomStringUtils.randomAlphabetic(20));
        assertEquals(one, null);
    }

    @Test
    public void findUserTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        final UserEntity[] userArray = addUser();
        final String idString = userArray[0].getId();
        final UserEntity originalEntity = userArray[1];

        User fromDbEntity = userService.getUser(idString);

        UserDetailsEntity originalDetails = originalEntity.getUserDetails();
        UserDetails fromDbDetails = fromDbEntity.getUserDetails();
        assertEquals(originalDetails.getFirstName(), fromDbDetails.getFirstName());
        assertEquals(originalDetails.getLastName(), fromDbDetails.getLastName());
        assertEquals(originalDetails.getJobTitle(), fromDbDetails.getJobTitle());
        assertEquals(originalDetails.getEmail(), fromDbDetails.getEmail());
        assertEquals(originalDetails.getPhone(), fromDbDetails.getPhone());
        assertEquals(originalDetails.getInstitution(), fromDbDetails.getInstitution());
        assertEquals(originalDetails.getInstitutionAbbreviation(), fromDbDetails.getInstitutionAbbreviation());
        assertEquals(originalDetails.getInstitutionWeb(), fromDbDetails.getInstitutionWeb());

        AddressEntity originalAddress = originalDetails.getAddress();
        Address fromDbAddress = fromDbDetails.getAddress();
        assertEquals(originalAddress.getAddress1(), fromDbAddress.getAddress1());
        assertEquals(originalAddress.getAddress2(), fromDbAddress.getAddress2());
        assertEquals(originalAddress.getCountry(), fromDbAddress.getCountry());
        assertEquals(originalAddress.getRegion(), fromDbAddress.getRegion());
        assertEquals(originalAddress.getCity(), fromDbAddress.getCity());
        assertEquals(originalAddress.getZipCode(), fromDbAddress.getZipCode());
    }

    @Test
    public void putUserTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();

        // get user and store the original last name
        User user = userService.getUser(idString);
        final String originalLastName = user.getUserDetails().getLastName();

        // change first name and put
        String newFirstName = RandomStringUtils.randomAlphabetic(20);
        userEntityArray[0].getUserDetails().setFirstName(newFirstName);

        userService.updateUser(idString, userEntityArray[0]);

        user = userService.getUser(idString);
        assertEquals(user.getUserDetails().getFirstName(), newFirstName);
        assertEquals(user.getUserDetails().getLastName(), originalLastName);
    }

    @Test
    public void updateUserNullFieldTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);

        final UserEntity userEntity = getUserEntity();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        savedUserEntity.getUserDetails().setFirstName(null);

        userService.updateUser(savedUserEntity.getId(), savedUserEntity);
    }

    @Test(expected = UserIdNullOrEmptyException.class)
    public void updateUserNullIdTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = new UserEntity();
        userService.updateUser(null, userEntity);
    }

    @Test(expected = UserIdNullOrEmptyException.class)
    public void updateUserEmptyIdTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = new UserEntity();
        userService.updateUser("", userEntity);
    }

    private UserEntity[] addUser() throws Exception {
        UserEntity userEntity = getUserEntity();
        UserEntity saveUser = userRepository.save(userEntity);

        final UserEntity[] userArray = new UserEntity[2];
        userArray[0] = saveUser;
        userArray[1] = userEntity;

        return userArray;
    }

    @Test
    public void addUserToTeamTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity[] userEntityArray = addUser();
        UserEntity userEntity = userEntityArray[0];
        String userId = userEntity.getId();
        String teamId = RandomStringUtils.randomAlphabetic(20);
        userEntity.addTeam(teamId);
        userService.updateUser(userId, userEntity);

        User userFromDb = userService.getUser(userId);
        List<String> teamList = userFromDb.getTeams();
        assertEquals(teamList.get(0), teamId);
    }

    @Test
    public void addUserTest() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);

        User userFromDb = userService.getUser(user.getId());
        assertEquals(userEntity.getUserDetails().getFirstName(), userFromDb.getUserDetails().getFirstName());
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void addUserTestUsernameExists() throws Exception {
        // try to create a user again with the same email
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        userService.createUser(userEntity);
        userService.createUser(userEntity);
    }

    @Test(expected = UserIdNullOrEmptyException.class)
    public void removeUserFromTeamTestUserIdNull() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        userService.removeTeam(null, RandomStringUtils.randomAlphanumeric(20));
    }

    @Test(expected = UserNotFoundException.class)
    public void removeUserFromTeamTestUserNotFound() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        userService.removeTeam(RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    public void removeUserFromTeamTestGood() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        String userId = user.getId();
        String teamId = RandomStringUtils.randomAlphabetic(20);

        userService.addTeam(userId, teamId);

        // ensure team is added from the user side
        User userFromDb = userService.getUser(userId);
        List<String> teamList = userFromDb.getTeams();
        assertEquals(teamList.get(0), teamId);

        // ensure team is removed from the user side
        userService.removeTeam(userId, teamId);

        User userFromDb2 = userService.getUser(userId);
        List<String> teamList2 = userFromDb2.getTeams();
        assertEquals(teamList2.isEmpty(), true);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusToCreated() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.CREATED);
    }

    @Test
    public void testUpdateUserStatusCreatedToPending() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        User user2 = userService.updateUserStatus(user.getId(), UserStatus.PENDING);
        assertEquals(user2.getStatus(), UserStatus.PENDING);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusCreatedToApproved() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.APPROVED);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusCreatedToRejected() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.REJECTED);
    }

    @Test
    public void testUpdateUserStatusPendingToApproved() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.PENDING);
        userService.updateUserStatus(user.getId(), UserStatus.APPROVED);
        User user2 = userService.getUser(user.getId());
        assertEquals(user2.getStatus(), UserStatus.APPROVED);
    }

    @Test
    public void testUpdateUserStatusPendingToRejected() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.PENDING);
        userService.updateUserStatus(user.getId(), UserStatus.REJECTED);
        User user2 = userService.getUser(user.getId());
        assertEquals(user2.getStatus(), UserStatus.REJECTED);
    }

    @Test
    public void testUpdateUserStatusToClosed() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.CLOSED);
        User user2 = userService.getUser(user.getId());
        assertEquals(user2.getStatus(), UserStatus.CLOSED);
    }
}
