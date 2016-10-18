package sg.ncl.service.user.data.jpa;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.user.AbstractTest;

/**
 * @author Christopher Zhong
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class UserCredentialsRepositoryTest extends AbstractTest {

    @Test
    public void test() throws Exception {
    }

}
