package sg.ncl.service.user.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christopher Zhong
 */
public class LoginActivityEntityTest {

    @Test
    public void testGetId() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();

        assertThat(loginActivityEntity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();
        final Long id = new Random().nextLong();
        loginActivityEntity.setId(id);

        assertThat(loginActivityEntity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetIpAddress() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();

        assertThat(loginActivityEntity.getIpAddress()).isNull();
    }

    @Test
    public void testSetIpAddress() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();
        final String ipAddress = RandomStringUtils.randomAlphanumeric(20);
        loginActivityEntity.setIpAddress(ipAddress);

        assertThat(loginActivityEntity.getIpAddress()).isEqualTo(ipAddress);
    }

    @Test
    public void testGetDate() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();

        assertThat(loginActivityEntity.getDate()).isNull();
    }

    @Test
    public void testSetDate() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();
        final ZonedDateTime now = ZonedDateTime.now();
        loginActivityEntity.setDate(now);

        assertThat(loginActivityEntity.getDate()).isEqualTo(now);
    }

    @Test
    public void testToString() throws Exception {
        final LoginActivityEntity loginActivityEntity = new LoginActivityEntity();
        final Long id = new Random().nextLong();
        loginActivityEntity.setId(id);
        final String ipAddress = RandomStringUtils.randomAlphanumeric(20);
        loginActivityEntity.setIpAddress(ipAddress);
        final ZonedDateTime now = ZonedDateTime.now();
        loginActivityEntity.setDate(now);

        final String toString = loginActivityEntity.toString();

        assertThat(toString).contains(id.toString());
        assertThat(toString).contains(ipAddress);
        assertThat(toString).contains(now.toString());
    }

}
