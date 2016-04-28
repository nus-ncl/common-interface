package sg.ncl.testbed_interface.repositories.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.testbed_interface.domain.UserCredentialsStatus;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class UserCredentialsEntityTest {

    @Test
    public void testGetId() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();

        assertThat(userCredentialsEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        final long id = new Random().nextLong();
        userCredentialsEntity.setId(id);

        assertThat(userCredentialsEntity.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetUsername() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();

        assertThat(userCredentialsEntity.getUsername(), is(nullValue()));
    }

    @Test
    public void testSetUsername() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        final String username = RandomStringUtils.random(20);
        userCredentialsEntity.setUsername(username);

        assertThat(userCredentialsEntity.getUsername(), is(equalTo(username)));
    }

    @Test
    public void testGetPassword() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();

        assertThat(userCredentialsEntity.getPassword(), is(nullValue()));
    }

    @Test
    public void testSetPassword() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        final String password = RandomStringUtils.random(20);
        userCredentialsEntity.setPassword(password);

        assertThat(userCredentialsEntity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testGetUser() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();

        assertThat(userCredentialsEntity.getUser(), is(nullValue()));
    }

    @Test
    public void testSetUser() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        final UserEntity userEntity = new UserEntity();
        userCredentialsEntity.setUser(userEntity);

        assertThat(userCredentialsEntity.getUser(), is(equalTo(userEntity)));
    }

    @Test
    public void testGetStatus() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();

        assertThat(userCredentialsEntity.getStatus(), is(equalTo(UserCredentialsStatus.ACTIVE)));
    }

    @Test
    public void testSetStatus() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        userCredentialsEntity.setStatus(UserCredentialsStatus.INACTIVE);

        assertThat(userCredentialsEntity.getStatus(), is(equalTo(UserCredentialsStatus.INACTIVE)));
    }

    @Test
    public void testEquals() throws Exception {
        final UserCredentialsEntity userCredentialsEntity1 = new UserCredentialsEntity();
        final UserCredentialsEntity userCredentialsEntity2 = new UserCredentialsEntity();

        assertThat(userCredentialsEntity1, is(equalTo(userCredentialsEntity2)));

        final String username1 = RandomStringUtils.random(20);
        userCredentialsEntity1.setUsername(username1);

        assertThat(userCredentialsEntity1, is(not(equalTo(userCredentialsEntity2))));

        userCredentialsEntity2.setUsername(username1);

        assertThat(userCredentialsEntity1, is(equalTo(userCredentialsEntity2)));

        final String username2 = RandomStringUtils.random(20);
        userCredentialsEntity2.setUsername(username2);

        assertThat(userCredentialsEntity1, is(not(equalTo(userCredentialsEntity2))));
    }

    @Test
    public void testHashCode() throws Exception {
        final UserCredentialsEntity userCredentialsEntity1 = new UserCredentialsEntity();
        final UserCredentialsEntity userCredentialsEntity2 = new UserCredentialsEntity();

        assertThat(userCredentialsEntity1.hashCode(), is(equalTo(userCredentialsEntity2.hashCode())));

        final String username1 = RandomStringUtils.random(20);
        userCredentialsEntity1.setUsername(username1);

        assertThat(userCredentialsEntity1.hashCode(), is(not(equalTo(userCredentialsEntity2.hashCode()))));

        userCredentialsEntity2.setUsername(username1);

        assertThat(userCredentialsEntity1.hashCode(), is(equalTo(userCredentialsEntity2.hashCode())));

        final String username2 = RandomStringUtils.random(20);
        userCredentialsEntity2.setUsername(username2);

        assertThat(userCredentialsEntity1.hashCode(), is(not(equalTo(userCredentialsEntity2.hashCode()))));
    }

    @Test
    public void testToString() throws Exception {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        final Long id = new Random().nextLong();
        userCredentialsEntity.setId(id);
        final String username = RandomStringUtils.randomAlphanumeric(20);
        userCredentialsEntity.setUsername(username);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        userCredentialsEntity.setPassword(password);
        userCredentialsEntity.setStatus(UserCredentialsStatus.ACTIVE);

        final String toString = userCredentialsEntity.toString();

        assertThat(toString, containsString(id.toString()));
        assertThat(toString, containsString(username));
        assertThat(toString, containsString(password));
        assertThat(toString, containsString(UserCredentialsStatus.ACTIVE.toString()));
    }

}