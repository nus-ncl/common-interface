package sg.ncl.service.authentication.data.jpa.repositories;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.Util;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class CredentialsRepositoryTest extends AbstractTest {

    @Inject
    private CredentialsRepository repository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(CredentialsRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final CredentialsEntity entity = Util.getCredentialsEntity();

        final long count = repository.count();
        final CredentialsEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getId(), is(equalTo(savedEntity.getId())));
    }

    @Test
    public void testSaveNullUsername() throws Exception {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        entity.setUsername(null);

        try {
            repository.save(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USERNAME\"");
        }
    }

    @Test
    public void testNullPassword() throws Exception {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        entity.setPassword(null);

        try {
            repository.save(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"PASSWORD\"");
        }
    }

    @Test
    public void testNullUserId() throws Exception {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        entity.setId(null);

        try {
            repository.save(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_ID\"");
        }
    }

    @Test
    public void testDuplicateUsername() throws Exception {
        final CredentialsEntity entity1 = Util.getCredentialsEntity();
        final CredentialsEntity entity2 = Util.getCredentialsEntity();
        entity2.setUsername(entity1.getUsername());

        assertThat(repository.findByUsername(entity1.getUsername()), is(nullValue(CredentialsEntity.class)));
        repository.save(entity1);
        try {
            repository.save(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "Unique index or primary key violation:", "PUBLIC.CREDENTIALS(USERNAME)");
        }
    }

    @Test
    public void testDuplicateUserId() throws Exception {
        final CredentialsEntity entity1 = Util.getCredentialsEntity();
        final CredentialsEntity entity2 = Util.getCredentialsEntity();
        entity2.setId(entity1.getId());

        assertThat(repository.findOne(entity1.getId()), is(nullValue(CredentialsEntity.class)));
        repository.save(entity1);
        try {
            repository.save(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "Unique index or primary key violation:", "PUBLIC.CREDENTIALS(USER_ID)");
        }
    }

    @Test
    @Ignore("updatable = false is not working")
    public void testNonUpdatableUserId() throws Exception {
        CredentialsEntity entity = Util.getCredentialsEntity();
        final String userId = entity.getId();

        final long count = repository.count();
        entity = repository.save(entity);
        final String id = entity.getId();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getId(), is(equalTo(id)));
        entity = repository.findOne(id);
        assertThat(entity.getId(), is(equalTo(userId)));
    }

}
