package sg.ncl.service.user.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import sg.ncl.service.user.exceptions.UserIdNullException;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Desmond
 */
public class UserServiceTestNew extends AbstractTest {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void before() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusToCreated() throws Exception {
        UserEntity userEntity = Util.getUserEntity();
        when(userRepository.findOne(Mockito.anyString())).thenReturn(userEntity);
        userService.updateUserStatus(RandomStringUtils.randomAlphanumeric(20), UserStatus.CREATED);
    }

    @Test
    public void testUpdateUserStatusCreatedToPending() throws Exception {
        UserEntity userEntity = Util.getUserEntity();
        when(userRepository.findOne(Mockito.anyString())).thenReturn(userEntity);
        User user = userService.updateUserStatus(RandomStringUtils.randomAlphanumeric(20), UserStatus.PENDING);
        Assert.assertEquals(user.getStatus(), UserStatus.PENDING);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusCreatedToApproved() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.APPROVED);
    }

    @Test(expected = InvalidStatusTransitionException.class)
    public void testUpdateUserStatusCreatedToRejected() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.REJECTED);
    }

    @Test
    public void testUpdateUserStatusPendingToApproved() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setStatus(UserStatus.PENDING);
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.APPROVED);
        User user2 = userService.getUser(user.getId());
        Assert.assertEquals(user2.getStatus(), UserStatus.APPROVED);
    }

    @Test
    public void testUpdateUserStatusPendingToRejected() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setStatus(UserStatus.PENDING);
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.REJECTED);
        User user2 = userService.getUser(user.getId());
        Assert.assertEquals(user2.getStatus(), UserStatus.REJECTED);
    }

    @Test
    public void testUpdateUserStatusToClosed() throws Exception {
        UserService userService = new UserServiceImpl(userRepository);
        UserEntity userEntity = Util.getUserEntity();
        User user = userService.createUser(userEntity);
        userService.updateUserStatus(user.getId(), UserStatus.CLOSED);
        User user2 = userService.getUser(user.getId());
        Assert.assertEquals(user2.getStatus(), UserStatus.CLOSED);
    }
}
