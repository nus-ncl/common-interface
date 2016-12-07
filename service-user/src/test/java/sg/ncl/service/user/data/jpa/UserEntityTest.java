package sg.ncl.service.user.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserAlreadyInTeamException;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christopher Zhong
 */
public class UserEntityTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetId() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final String random = RandomStringUtils.random(20);
        userEntity.setId(random);

        assertThat(userEntity.getId()).isEqualTo(random);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getUserDetails()).isNull();
    }

    @Test
    public void testSetUserDetails() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userEntity.setUserDetails(userDetailsEntity);

        assertThat(userEntity.getUserDetails()).isEqualTo(userDetailsEntity);
    }

    @Test
    public void testGetVerificationKey() throws Exception {
        final UserEntity userEntity = Util.getUserEntity();

        assertThat(userEntity.getVerificationKey()).isNotEmpty();
    }

    @Test
    public void testIsEmailVerified() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.isEmailVerified()).isEqualTo(false);
    }

    @Test
    public void testSetEmailVerified() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmailVerified(true);

        assertThat(userEntity.isEmailVerified()).isEqualTo(true);
    }

    @Test
    public void testGetStatus() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.CREATED);
    }

    @Test
    public void testSetStatus() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setStatus(UserStatus.APPROVED);

        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.APPROVED);
    }

    @Test
    public void testGetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getApplicationDate()).isNull();
    }

    @Test
    public void testSetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        userEntity.setApplicationDate(now);

        assertThat(userEntity.getApplicationDate()).isEqualTo(now);
    }

    @Test
    public void testGetProcessedDate() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getProcessedDate()).isNull();
    }

    @Test
    public void testSetProcessedDate() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        userEntity.setProcessedDate(now);

        assertThat(userEntity.getProcessedDate()).isEqualTo(now);
    }

    @Test
    public void testGetLoginActivities() throws Exception {
        final UserEntity userEntity = new UserEntity();

        final List<LoginActivityEntity> loginActivities = userEntity.getLoginActivities();

        assertThat(loginActivities).isNotNull();
        assertThat(loginActivities).isEmpty();
    }

    @Test
    public void testAddLoginActivity() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final LoginActivityEntity loginActivity1 = new LoginActivityEntity();
        userEntity.addLoginActivity(loginActivity1);

        final List<LoginActivityEntity> loginActivities = userEntity.getLoginActivities();

        assertThat(loginActivities).isNotNull();
        assertThat(loginActivities).isNotEmpty();
        assertThat(loginActivities.size()).isEqualTo(1);
        assertThat(loginActivities).contains(loginActivity1);

        final LoginActivityEntity loginActivity2 = new LoginActivityEntity();
        userEntity.addLoginActivity(loginActivity2);

        assertThat(loginActivities.size()).isEqualTo(2);
        assertThat(loginActivities).contains(loginActivity1, loginActivity2);
    }

    @Test
    public void testEquals() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1).isEqualTo(userEntity2);

        final String id1 = "id1";
        userEntity1.setId(id1);

        assertThat(userEntity1).isNotEqualTo(userEntity2);

        userEntity2.setId(id1);

        assertThat(userEntity1).isEqualTo(userEntity2);

        final String id2 = "id2";
        userEntity2.setId(id2);

        assertThat(userEntity1).isNotEqualTo(userEntity2);
    }

    @Test
    public void testEqualsThis() {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = userEntity1;

        assertThat(userEntity1).isEqualTo(userEntity2);
    }

    @Test
    public void testEqualsNull() {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = null;

        assertThat(userEntity1).isNotEqualTo(userEntity2);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        final UserEntity userEntity1 = new UserEntity();
        final UserDetailsEntity userEntity2 = new UserDetailsEntity();

        assertThat(userEntity1).isNotEqualTo(userEntity2);
    }

    @Test
    public void testHashCode() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1.hashCode()).isEqualTo(userEntity2.hashCode());

        final String id1 = "id1";
        userEntity1.setId(id1);

        assertThat(userEntity1.hashCode()).isNotEqualTo(userEntity2.hashCode());

        userEntity2.setId(id1);

        assertThat(userEntity1.hashCode()).isEqualTo(userEntity2.hashCode());

        final String id2 = "id2";
        userEntity2.setId(id2);

        assertThat(userEntity1.hashCode()).isNotEqualTo(userEntity2.hashCode());
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

        assertThat(toString).contains(id);
        assertThat(toString).contains(status.toString());
        assertThat(toString).contains(String.valueOf(true));
        assertThat(toString).contains(String.valueOf("null"));
        assertThat(toString).contains(registrationDate.toString());
        assertThat(toString).contains(processedDate.toString());
    }

    @Test
    public void testAddTeamAlreadyExist() throws Exception {
        final UserEntity userEntity = Util.getUserEntity();
        String teamId = RandomStringUtils.randomAlphanumeric(20);
        exception.expect(UserAlreadyInTeamException.class);
        userEntity.addTeam(teamId);
        userEntity.addTeam(teamId);
    }

    @Test
    public void testRemoveTeamNotFound() throws Exception {
        final UserEntity userEntity = Util.getUserEntity();
        String teamId_1 = RandomStringUtils.randomAlphanumeric(20);
        String teamId_2 = RandomStringUtils.randomAlphanumeric(20);

        userEntity.addTeam(teamId_1);
        userEntity.addTeam(teamId_2);

        // first assert that the team don't exist
        List<String> teamsList = userEntity.getTeams();

        assertThat(teamsList.size()).isEqualTo(2);

        userEntity.removeTeam(RandomStringUtils.randomAlphanumeric(20));

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

        userEntity.addTeam(teamId_1);
        userEntity.addTeam(teamId_2);

        // first assert that the team don't exist
        List<String> teamsList = userEntity.getTeams();

        assertThat(teamsList.size()).isEqualTo(2);

        userEntity.removeTeam(teamId_2);

        // finally assert that only teamId_2 is removed
        List<String> result = userEntity.getTeams();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(teamId_1);
    }

}
