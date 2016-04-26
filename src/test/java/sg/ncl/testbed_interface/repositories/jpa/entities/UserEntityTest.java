package sg.ncl.testbed_interface.repositories.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.testbed_interface.domain.UserStatus;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.*;
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

        assertThat(userEntity.getStatus(), is(equalTo(UserStatus.PENDING)));
    }

    @Test
    public void testSetStatus() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setStatus(UserStatus.APPROVED);

        assertThat(userEntity.getStatus(), is(equalTo(UserStatus.APPROVED)));
    }

    @Test
    public void testGetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();

        assertThat(userEntity.getRegistrationDate(), is(nullValue()));
    }

    @Test
    public void testSetRegistrationDate() throws Exception {
        final UserEntity userEntity = new UserEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        userEntity.setRegistrationDate(now);

        assertThat(userEntity.getRegistrationDate(), is(equalTo(now)));
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
    public void testEquals() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1, is(equalTo(userEntity2)));

        final String id1 = "id1";
        final String id2 = "id2";
        userEntity1.setId(id1);

        assertThat(userEntity1, is(not(equalTo(userEntity2))));

        userEntity2.setId(id1);

        assertThat(userEntity1, is(equalTo(userEntity2)));

        userEntity2.setId(id2);

        assertThat(userEntity1, is(not(equalTo(userEntity2))));
    }

    @Test
    public void testHashCode() throws Exception {
        final UserEntity userEntity1 = new UserEntity();
        final UserEntity userEntity2 = new UserEntity();

        assertThat(userEntity1.hashCode(), is(equalTo(userEntity2.hashCode())));

        final String id1 = "id1";
        final String id2 = "id2";
        userEntity1.setId(id1);

        assertThat(userEntity1.hashCode(), is(not(equalTo(userEntity2.hashCode()))));

        userEntity2.setId(id1);

        assertThat(userEntity1.hashCode(), is(equalTo(userEntity2.hashCode())));

        userEntity2.setId(id2);

        assertThat(userEntity1.hashCode(), is(not(equalTo(userEntity2.hashCode()))));
    }

}