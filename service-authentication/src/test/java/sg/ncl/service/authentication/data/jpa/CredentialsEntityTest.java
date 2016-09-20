package sg.ncl.service.authentication.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class CredentialsEntityTest {

    @Test
    public void testGetId() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUsername() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getUsername()).isNull();
        ;
    }

    @Test
    public void testSetUsername() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String username = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsername(username);

        assertThat(entity.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetPassword() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getPassword()).isNull();
    }

    @Test
    public void testSetPassword() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        final String password = RandomStringUtils.randomAlphanumeric(20);
        entity.setPassword(password);

        assertThat(entity.getPassword()).isEqualTo(password);
    }

    @Test
    public void testGetStatus() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
    }

    @Test
    public void testSetStatus() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.setStatus(CredentialsStatus.INACTIVE);

        assertThat(entity.getStatus(), is(equalTo(CredentialsStatus.INACTIVE)));
    }

    @Test
    public void testGetRoles() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();

        assertThat(entity.getRoles()).isEmpty();
    }

    @Test
    public void testAddRole() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.addRole(Role.ADMIN);
        entity.addRole(Role.ADMIN);

        assertThat(entity.getRoles()).containsOnly(Role.ADMIN).hasSize(1);
    }

    @Test
    public void testRemoveRole() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.addRole(Role.ADMIN);
        entity.addRole(Role.USER);

        entity.removeRole(Role.USER);

        assertThat(entity.getRoles()).containsOnly(Role.ADMIN).hasSize(1);
    }

    @Test
    public void testRemoveRoleNotInSet() throws Exception {
        final CredentialsEntity entity = new CredentialsEntity();
        entity.addRole(Role.USER);

        entity.removeRole(Role.ADMIN);

        assertThat(entity.getRoles()).containsOnly(Role.USER).hasSize(1);
    }

    @Test
    public void testEqualsSameInstanceNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = entity1;

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceNonNullIdCompareToNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceSameNonNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);
        entity2.setId(id1);

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceDifferentNonNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);
        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    public void testEqualsNullObject() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();

        assertThat(entity1.equals(null)).isFalse();
    }

    @Test
    public void testEqualsDifferentClass() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();

        assertThat(entity1.equals("")).isFalse();
    }

    @Test
    public void testHashCodeSameInstanceNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = entity1;

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode()).isEqualTo(0);
    }

    @Test
    public void testHashCodeDifferentInstanceNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode()).isEqualTo(0);
    }

    @Test
    public void testHashCodeDifferentInstanceNonNullIdCompareToNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testHashCodeDifferentInstanceSameNonNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);
        entity2.setId(id1);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    public void testHashCodeDifferentInstanceDifferentNonNullId() throws Exception {
        final CredentialsEntity entity1 = new CredentialsEntity();
        final CredentialsEntity entity2 = new CredentialsEntity();
        final String id1 = RandomStringUtils.randomAlphanumeric(20);
        entity1.setId(id1);
        final String id2 = RandomStringUtils.randomAlphanumeric(20);
        entity2.setId(id2);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        final CredentialsEntity entity = TestUtil.getCredentialsEntity();

        assertThat(entity.toString()).contains(entity.getId(), entity.getUsername(), entity.getPassword(), CredentialsStatus.ACTIVE.toString(), Role.USER.toString());
    }

}
