package sg.ncl.service.authentication.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by dcszwang on 11/8/2016.
 */
public class PasswordResetRequestEntityTest {

    @Test
    public void testGetId() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();

        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        final Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetUsername() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();

        assertThat(entity.getUsername()).isNull();
    }

    @Test
    public void testSetUsername() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        final String username = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsername(username);

        assertThat(entity.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetHash() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();

        assertThat(entity.getHash()).isNull();
    }

    @Test
    public void testSetHash() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        final String hash = RandomStringUtils.randomAlphanumeric(20);
        entity.setHash(hash);

        assertThat(entity.getHash()).isEqualTo(hash);
    }

    @Test
    public void testGetTime() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();

        assertThat(entity.getTime()).isNull();
    }

    @Test
    public void testSetTime() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        ZonedDateTime now = ZonedDateTime.now();
        entity.setTime(now);

        Assert.assertThat(entity.getTime(), is(equalTo(now)));
    }

    @Test
    public void testEqualsSameInstanceNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = entity1;

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceNonNullIdCompareToNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceSameNonNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);
        entity2.setId(id1);

        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    public void testEqualsDifferentInstanceDifferentNonNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);
        final Long id2 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity2.setId(id2);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    public void testEqualsNullObject() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();

        assertThat(entity1.equals(null)).isFalse();
    }

    @Test
    public void testEqualsDifferentClass() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();

        assertThat(entity1.equals("")).isFalse();
    }

    @Test
    public void testHashCodeSameInstanceNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = entity1;

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode()).isEqualTo(0);
    }

    @Test
    public void testHashCodeDifferentInstanceNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode()).isEqualTo(0);
    }

    @Test
    public void testHashCodeDifferentInstanceNonNullIdCompareToNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testHashCodeDifferentInstanceSameNonNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);
        entity2.setId(id1);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    public void testHashCodeDifferentInstanceDifferentNonNullId() throws Exception {
        final PasswordResetRequestEntity entity1 = new PasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = new PasswordResetRequestEntity();
        final Long id1 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity1.setId(id1);
        final Long id2 = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity2.setId(id2);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        final PasswordResetRequestEntity entity = new PasswordResetRequestEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setTime(ZonedDateTime.now());
        entity.setHash(RandomStringUtils.randomAlphanumeric(20));
        entity.setUsername(RandomStringUtils.randomAlphabetic(10));

        assertThat(entity.toString()).contains(entity.getId().toString(), entity.getUsername(), entity.getHash(), entity.getTime().toString());
    }

}
