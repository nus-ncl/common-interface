package sg.ncl.service.authentication.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.authentication.Util;
import sg.ncl.service.authentication.domain.CredentialsStatus;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class CredentialsEntityTest {

    @Test
    public void testGetId() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);

        assertThat(entity.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetUsername() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getUsername(), is(nullValue()));
    }

    @Test
    public void testSetUsername() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String username = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsername(username);

        assertThat(entity.getUsername(), is(equalTo(username)));
    }

    @Test
    public void testGetPassword() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getPassword(), is(nullValue()));
    }

    @Test
    public void testSetPassword() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String password = RandomStringUtils.randomAlphanumeric(20);
        entity.setPassword(password);

        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testGetStatus() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getStatus(), is(equalTo(CredentialsStatus.ACTIVE)));
    }

    @Test
    public void testSetStatus() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.setStatus(CredentialsStatus.INACTIVE);

        assertThat(entity.getStatus(), is(equalTo(CredentialsStatus.INACTIVE)));
    }

    @Test
    public void testEquals() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();

        assertThat(entity1, is(equalTo(entity2)));

        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);

        assertThat(entity1, is(not(equalTo(entity2))));

        entity2.setId(id1);

        assertThat(entity1, is(equalTo(entity2)));

        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);

        assertThat(entity1, is(not(equalTo(entity2))));
    }

    @Test
    public void testHashCode() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();

        assertThat(entity1.hashCode(), is(equalTo(entity2.hashCode())));

        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);

        assertThat(entity1.hashCode(), is(not(equalTo(entity2.hashCode()))));

        entity2.setId(id1);

        assertThat(entity1.hashCode(), is(equalTo(entity2.hashCode())));

        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);

        assertThat(entity1.hashCode(), is(not(equalTo(entity2.hashCode()))));
    }

    @Test
    public void testToString() throws Exception {
        final CredentialsEntity entity = Util.getCredentialsEntity();

        final String toString = entity.toString();

        assertThat(toString, containsString(entity.getId()));
        assertThat(toString, containsString(entity.getUsername()));
        assertThat(toString, containsString(entity.getPassword()));
        assertThat(toString, containsString(CredentialsStatus.ACTIVE.toString()));
    }

}
