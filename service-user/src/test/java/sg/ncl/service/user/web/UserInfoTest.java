package sg.ncl.service.user.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.user.domain.UserStatus;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 08-Dec-16.
 */
public class UserInfoTest {

    private final String id = RandomStringUtils.randomAlphanumeric(20);
    private final boolean emailVerified = true;
    private final String verificationKey = RandomStringUtils.randomAlphanumeric(20);
    private final UserStatus status = UserStatus.CREATED;
    private final ZonedDateTime applicationDate = ZonedDateTime.now();
    private final ZonedDateTime processedDate = ZonedDateTime.now();

    private final UserInfo userInfo = new UserInfo(id, null, emailVerified, verificationKey, status, applicationDate, processedDate, null, null);

    @Test
    public void testGetId() throws Exception {
        assertThat(userInfo.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        assertThat(userInfo.getUserDetails()).isNull();
    }

    @Test
    public void testGetVerificationKey() throws Exception {
        assertThat(userInfo.getVerificationKey()).isEqualTo(verificationKey);
    }

    @Test
    public void testGetStatus() throws Exception {
        assertThat(userInfo.getStatus()).isEqualTo(status);
    }

    @Test
    public void testGetApplicationDate() throws Exception {
        assertThat(userInfo.getApplicationDate()).isEqualTo(applicationDate);
    }

    @Test
    public void testGetProcessedDate() throws Exception {
        assertThat(userInfo.getProcessedDate()).isEqualTo(processedDate);
    }

    @Test
    public void testGetLoginActivities() throws Exception {
        assertThat(userInfo.getLoginActivities()).isNull();
    }

    @Test
    public void testGetTeams() throws Exception {
        assertThat(userInfo.getTeams()).isNull();
    }
}