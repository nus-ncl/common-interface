package sg.ncl.service.user;

import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.User;

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
        List<User> list = userService.get();
        Assert.assertTrue(list.size() == 0);
    }


}
