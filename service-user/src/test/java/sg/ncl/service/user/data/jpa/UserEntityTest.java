package sg.ncl.service.user.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class UserEntityTest {

    @Test
    public void testGetId() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final String random = RandomStringUtils.random(20);
        userEntity.setId(random);

        assertThat(userEntity.getId(), is(equalTo(random)));
    }

    @Test
    public void testGetUserDetails() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getUserDetails(), is(nullValue()));
    }

    @Test
    public void testSetUserDetails() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userEntity.setUserDetails(userDetailsEntity);

        assertThat(userEntity.getUserDetails(), is(equalTo(userDetailsEntity)));
    }

    @Test
    public void testIsEmailVerified() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.isEmailVerified(), is(equalTo(false)));
    }

    @Test
    public void testSetEmailVerified() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmailVerified(true);

        assertThat(userEntity.isEmailVerified(), is(equalTo(true)));
    }

    @Test
    public void testGetStatus() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getStatus(), is(equalTo(UserStatus.CREATED)));
    }

    @Test
    public void testSetStatus() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setStatus(UserStatus.APPROVED);

        assertThat(userEntity.getStatus(), is(equalTo(UserStatus.APPROVED)));
    }

    @Test
    public void testGetRoles() throws Exception {
        final UserEntity userEntity = new UserEntity();
        Set<User.Role> result = new HashSet<>();
        assertThat(userEntity.getRoles().isEmpty(), is(true));
        assertThat(userEntity.getRoles(), is(equalTo(result)));
    }

    @Test
    public void testAddRoles() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.addRole(User.Role.ADMIN);
        Set<User.Role> result = new HashSet<>(Arrays.asList(User.Role.ADMIN));

        assertThat(userEntity.getRoles().size(), is(1));
        assertThat(userEntity.getRoles(), is(equalTo(result)));
    }

    @Test
    public void testRemoveRoles() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.addRole(User.Role.ADMIN);
        userEntity.addRole(User.Role.USER);
        userEntity.removeRole(User.Role.USER);
        Set<User.Role> result = new HashSet<>(Arrays.asList(User.Role.ADMIN));

        assertThat(userEntity.getRoles().size(), is(1));
        assertThat(userEntity.getRoles(), is(equalTo(result)));
    }

    @Test
    public void testGetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getApplicationDate(), is(nullValue()));
    }

    @Test
    public void testSetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        userEntity.setApplicationDate(now);

        assertThat(userEntity.getApplicationDate(), is(equalTo(now)));
    }

    @Test
    public void testGetProcessedDate() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getProcessedDate(), is(nullValue()));
    }

    @Test
    public void testSetProcessedDate() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        userEntity.setProcessedDate(now);

        assertThat(userEntity.getProcessedDate(), is(equalTo(now)));
    }

    @Test
    public void testGetLoginActivities() throws Exception {
        final UserEntity userEntity = new UserEntity();

        final List<LoginActivityEntity> loginActivities = userEntity.getLoginActivities();
        assertThat(loginActivities, is(not(nullValue())));
        assertThat(loginActivities, is(empty()));
    }

    @Test
    public void testAddLoginActivity() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final LoginActivityEntity loginActivity1 = new LoginActivityEntity();
        userEntity.addLoginActivity(loginActivity1);

        final List<LoginActivityEntity> loginActivities = userEntity.getLoginActivities();

        assertThat(loginActivities, is(not(nullValue())));
        assertThat(loginActivities, is(not(empty())));
        assertThat(loginActivities, contains(loginActivity1));
        assertThat(loginActivities.size(), is(equalTo(1)));

        final LoginActivityEntity loginActivity2 = new LoginActivityEntity();
        userEntity.addLoginActivity(loginActivity2);

        assertThat(loginActivities, contains(loginActivity1, loginActivity2));
        assertThat(loginActivities.size(), is(equalTo(2)));
    }

    @Test
    public void testEquals() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1, is(equalTo(userEntity2)));

        final String id1 = "id1";
        userEntity1.setId(id1);

        assertThat(userEntity1, is(not(equalTo(userEntity2))));

        userEntity2.setId(id1);

        assertThat(userEntity1, is(equalTo(userEntity2)));

        final String id2 = "id2";
        userEntity2.setId(id2);

        assertThat(userEntity1, is(not(equalTo(userEntity2))));
    }

    @Test
    public void testHashCode() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1.hashCode(), is(equalTo(userEntity2.hashCode())));

        final String id1 = "id1";
        userEntity1.setId(id1);

        assertThat(userEntity1.hashCode(), is(not(equalTo(userEntity2.hashCode()))));

        userEntity2.setId(id1);

        assertThat(userEntity1.hashCode(), is(equalTo(userEntity2.hashCode())));

        final String id2 = "id2";
        userEntity2.setId(id2);

        assertThat(userEntity1.hashCode(), is(not(equalTo(userEntity2.hashCode()))));
    }

    @Test
    public void testToString() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        userEntity.setId(id);
        final UserStatus status = UserStatus.APPROVED;
        userEntity.setStatus(status);
        userEntity.setEmailVerified(true);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime registrationDate = now.minusYears(1);
        userEntity.setApplicationDate(registrationDate);
        final ZonedDateTime processedDate = now.minusMonths(1);
        userEntity.setProcessedDate(processedDate);

        final String toString = userEntity.toString();

        assertThat(toString, containsString(id));
        assertThat(toString, containsString(status.toString()));
        assertThat(toString, containsString(String.valueOf(true)));
        assertThat(toString, containsString(String.valueOf("null")));
        assertThat(toString, containsString(registrationDate.toString()));
        assertThat(toString, containsString(processedDate.toString()));
    }

    @Test
    public void testRemoveTeamNotFound() throws Exception {
        final UserEntity userEntity = Util.getUserEntity();
        String teamId_1 = RandomStringUtils.randomAlphanumeric(20);
        String teamId_2 = RandomStringUtils.randomAlphanumeric(20);

        userEntity.addTeamId(teamId_1);
        userEntity.addTeamId(teamId_2);

        // first assert that the team don't exist
        List<String> teamsList = userEntity.getTeams();

        assertThat(teamsList.size(), is(2));

        userEntity.removeTeamId(RandomStringUtils.randomAlphanumeric(20));

        // finally assert that initial added teams have not been removed
        List<String> result = userEntity.getTeams();

        for (String teamId : result) {
            if (!teamId.equals(teamId_1) && !teamId.equals(teamId_2)) {
                Assert.fail("One of the team ids has been wrongly removed!");
            }
        }
    }

    @Test
    public void testRemoveTeamGood() throws Exception {
        final UserEntity userEntity = Util.getUserEntity();
        String teamId_1 = RandomStringUtils.randomAlphanumeric(20);
        String teamId_2 = RandomStringUtils.randomAlphanumeric(20);

        userEntity.addTeamId(teamId_1);
        userEntity.addTeamId(teamId_2);

        // first assert that the team don't exist
        List<String> teamsList = userEntity.getTeams();

        assertThat(teamsList.size(), is(2));

        userEntity.removeTeamId(teamId_2);

        // finally assert that only teamId_2 is removed
        List<String> result = userEntity.getTeams();

        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(teamId_1));
    }

}
