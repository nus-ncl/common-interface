package sg.ncl.service.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
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
        List<User> list = userService.get();
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

        List<User> userList2 = userService.get();

        Assert.assertThat(userList2, IsIterableContainingInAnyOrder.containsInAnyOrder(userArray));
    }

    @Test(expected=UserNotFoundException.class)
    public void getUserWithNoUserInDbTest() throws Exception {
        UserService userService = new UserService(userRepository);
        userService.find(RandomStringUtils.randomAlphabetic(20));
    }

    private UserEntity[] addUser() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setPhone(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomAlphabetic(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        UserEntity saveUser = userRepository.save(userEntity);

        final UserEntity[] userArray = new UserEntity[2];
        userArray[0] = saveUser;
        userArray[1] = userEntity;

        return userArray;
    }
}
