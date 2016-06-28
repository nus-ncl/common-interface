package sg.ncl.service.user.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Desmond
 */
public class UserServiceTest extends AbstractTest {
    @Inject
    private UserRepository userRepository;

    @Test
    public void getAllUserWithNoUserInDbTest() throws Exception {
        UserService userService = new UserService(userRepository);
        List<User> list = userService.getAll();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void getAllUserTest() throws Exception {
        UserService userService = new UserService(userRepository);
        User[] userArray = new User[3];

        for (int i = 0; i < 3; i++) {
            UserEntity[] userEntityArray = addUser();
            userArray[i] = userEntityArray[0];
        }

        List<User> userList2 = userService.getAll();

        Assert.assertThat(userList2, IsIterableContainingInAnyOrder.containsInAnyOrder(userArray));
    }

    @Test(expected = UserIdNullException.class)
    public void getUserWithNullIdTest() throws Exception {
        UserService userService = new UserService(userRepository);
        userService.find(null);
    }

    @Test(expected = UserIdNullException.class)
    public void getUserWithEmptyIdTest() throws Exception {
        UserService userService = new UserService(userRepository);
        userService.find("");
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserWithNoUserInDbTest() throws Exception {
        UserService userService = new UserService(userRepository);
        userService.find(RandomStringUtils.randomAlphabetic(20));
    }

    @Test
    public void findUserTest() throws Exception {
        UserService userService = new UserService(userRepository);
        final UserEntity[] userArray = addUser();
        final String idString = userArray[0].getId();
        final UserEntity originalEntity = userArray[1];

        UserEntity fromDbEntity = userService.find(idString);

        UserDetailsEntity originalDetails = originalEntity.getUserDetails();
        UserDetailsEntity fromDbDetails = fromDbEntity.getUserDetails();
        Assert.assertEquals(originalDetails.getFirstName(), fromDbDetails.getFirstName());
        Assert.assertEquals(originalDetails.getLastName(), fromDbDetails.getLastName());
        Assert.assertEquals(originalDetails.getJobTitle(), fromDbDetails.getJobTitle());
        Assert.assertEquals(originalDetails.getEmail(), fromDbDetails.getEmail());
        Assert.assertEquals(originalDetails.getPhone(), fromDbDetails.getPhone());
        Assert.assertEquals(originalDetails.getInstitution(), fromDbDetails.getInstitution());
        Assert.assertEquals(originalDetails.getInstitutionAbbreviation(), fromDbDetails.getInstitutionAbbreviation());
        Assert.assertEquals(originalDetails.getInstitutionWeb(), fromDbDetails.getInstitutionWeb());

        AddressEntity originalAddress = originalDetails.getAddress();
        AddressEntity fromDbAddress = fromDbDetails.getAddress();
        Assert.assertEquals(originalAddress.getAddress1(), fromDbAddress.getAddress1());
        Assert.assertEquals(originalAddress.getAddress2(), fromDbAddress.getAddress2());
        Assert.assertEquals(originalAddress.getCountry(), fromDbAddress.getCountry());
        Assert.assertEquals(originalAddress.getRegion(), fromDbAddress.getRegion());
        Assert.assertEquals(originalAddress.getCity(), fromDbAddress.getCity());
        Assert.assertEquals(originalAddress.getZipCode(), fromDbAddress.getZipCode());
    }

    @Test
    public void putUserTest() throws Exception {
        UserService userService = new UserService(userRepository);
        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();

        // get user and store the original last name
        UserEntity userEntity = userService.find(idString);
        final String originalLastName = userEntity.getUserDetails().getLastName();

        // change first name and put
        String newFirstName = RandomStringUtils.randomAlphabetic(20);
        userEntity.getUserDetails().setFirstName(newFirstName);

        userService.update(idString, userEntity.getUserDetails());

        userEntity = userService.find(idString);
        Assert.assertEquals(userEntity.getUserDetails().getFirstName(), newFirstName);
        Assert.assertEquals(userEntity.getUserDetails().getLastName(), originalLastName);
    }

    @Test
    public void updateUserNullFieldTest() throws Exception {
        UserService userService = new UserService(userRepository);
        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();

        // get user and store the original last name
        UserEntity userEntity = userService.find(idString);

        // change first name and put
        userEntity.getUserDetails().setFirstName(null);

        userService.update(idString, userEntity.getUserDetails());
    }

    @Test(expected = UserIdNullException.class)
    public void updateUserNullIdTest() throws Exception {
        UserService userService = new UserService(userRepository);
        UserEntity userEntity = new UserEntity();
        userService.update(null, userEntity.getUserDetails());
    }

    @Test(expected = UserIdNullException.class)
    public void updateUserEmptyIdTest() throws Exception {
        UserService userService = new UserService(userRepository);
        UserEntity userEntity = new UserEntity();
        userService.update("", userEntity.getUserDetails());
    }

    private UserEntity[] addUser() throws Exception {
        UserEntity userEntity = Util.getUserEntity();
        UserEntity saveUser = userRepository.save(userEntity);

        final UserEntity[] userArray = new UserEntity[2];
        userArray[0] = saveUser;
        userArray[1] = userEntity;

        return userArray;
    }

//    @Test
//    public void addNoSuchUserToTeamTest() throws Exception {
//        UserService userService = new UserService(userRepository);
//        boolean returnBoolean = userService.addUserToTeam("123456", "123456");
//        Assert.assertFalse(returnBoolean);
//    }

    @Test
    public void addUserToTeamTest() throws  Exception {
        UserService userService = new UserService(userRepository);
        UserEntity[] userEntityArray = addUser();
        UserEntity userEntity = userEntityArray[0];
        String userId = userEntity.getId();
        String teamId = RandomStringUtils.randomAlphabetic(20);
        userEntity.addTeamId(teamId);
        userService.update(userId, userEntity.getUserDetails());

        UserEntity userEntityFromDb = userService.find(userId);
        List<String> teamList = userEntityFromDb.getTeamIds();
        Assert.assertEquals(teamList.get(0), teamId);
    }

    @Test
    public void addUserTest() throws Exception {
        UserService userService = new UserService(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        User user = userService.createUser(userEntity);

        UserEntity userEntityFromDb = userService.find(user.getId());
        Assert.assertEquals(userEntity.getUserDetails(), userEntityFromDb.getUserDetails());
    }
}
