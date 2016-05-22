package sg.ncl.service.user.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Christopher Zhong
 */
public class UserDetailsEntityTest {

    @Test
    public void testGetId() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final Long id = new Random().nextLong();
        userDetailsEntity.setId(id);

        assertThat(userDetailsEntity.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetFirstName() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getFirstName(), is(nullValue()));
    }

    @Test
    public void testSetFirstName() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final String firstName = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setFirstName(firstName);

        assertThat(userDetailsEntity.getFirstName(), is(equalTo(firstName)));
    }

    @Test
    public void testGetLastName() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getLastName(), is(nullValue()));
    }

    @Test
    public void testSetLastName() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final String lastName = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setLastName(lastName);

        assertThat(userDetailsEntity.getLastName(), is(equalTo(lastName)));
    }

    @Test
    public void testGetAddress() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getAddress(), is(nullValue()));
    }

    @Test
    public void testSetAddress() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final AddressEntity addressEntity = new AddressEntity();
        userDetailsEntity.setAddress(addressEntity);

        assertThat(userDetailsEntity.getAddress(), is(equalTo(addressEntity)));
    }

    @Test
    public void testGetEmail() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getEmail(), is(nullValue()));
    }

    @Test
    public void testSetEmail() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final String email = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setEmail(email);

        assertThat(userDetailsEntity.getEmail(), is(equalTo(email)));
    }

    @Test
    public void testGetPhone() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        assertThat(userDetailsEntity.getPhone(), is(nullValue()));
    }

    @Test
    public void testSetPhone() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final String phone = RandomStringUtils.randomNumeric(10);
        userDetailsEntity.setPhone(phone);

        assertThat(userDetailsEntity.getPhone(), is(equalTo(phone)));
    }

    @Test
    public void testEquals() throws Exception {
        final UserDetailsEntity userDetailsEntity1 = new UserDetailsEntity();
        final UserDetailsEntity userDetailsEntity2 = new UserDetailsEntity();

        assertThat(userDetailsEntity1, is(equalTo(userDetailsEntity2)));

        final String email1 = "email1";
        userDetailsEntity1.setEmail(email1);

        assertThat(userDetailsEntity1, is(not(equalTo(userDetailsEntity2))));

        userDetailsEntity2.setEmail(email1);

        assertThat(userDetailsEntity1, is(equalTo(userDetailsEntity2)));

        final String email2 = "email2";
        userDetailsEntity2.setEmail(email2);

        assertThat(userDetailsEntity1, is(not(equalTo(userDetailsEntity2))));
    }

    @Test
    public void testHashCode() throws Exception {
        final UserDetailsEntity userDetailsEntity1 = new UserDetailsEntity();
        final UserDetailsEntity userDetailsEntity2 = new UserDetailsEntity();

        assertThat(userDetailsEntity1.hashCode(), is(equalTo(userDetailsEntity2.hashCode())));

        final String email1 = "email1";
        userDetailsEntity1.setEmail(email1);

        assertThat(userDetailsEntity1.hashCode(), is(not(equalTo(userDetailsEntity2.hashCode()))));

        userDetailsEntity2.setEmail(email1);

        assertThat(userDetailsEntity1.hashCode(), is(equalTo(userDetailsEntity2.hashCode())));

        final String email2 = "email2";
        userDetailsEntity2.setEmail(email2);

        assertThat(userDetailsEntity1.hashCode(), is(not(equalTo(userDetailsEntity2.hashCode()))));
    }

    @Test
    public void testToString() throws Exception {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final Random random = new Random();
        final Long id = random.nextLong();
        userDetailsEntity.setId(id);
        final String firstName = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setFirstName(firstName);
        final String lastName = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setLastName(lastName);
        final String email = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setEmail(email);
        final String phone = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setPhone(phone);

        final String toString = userDetailsEntity.toString();

        assertThat(toString, containsString(String.valueOf(id)));
        assertThat(toString, containsString(firstName));
        assertThat(toString, containsString(lastName));
        assertThat(toString, containsString(email));
        assertThat(toString, containsString(phone));
    }

}
