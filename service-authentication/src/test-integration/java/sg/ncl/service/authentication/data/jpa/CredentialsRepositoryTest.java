package sg.ncl.service.authentication.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import sg.ncl.service.authentication.AbstractTest;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;

/**
 * @author Christopher Zhong
 */
public class CredentialsRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private CredentialsRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(CredentialsRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();

        final long count = repository.count();
        final CredentialsEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getId(), is(equalTo(savedEntity.getId())));
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSaveNullId() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();
        entity.setId(null);

        exception.expect(JpaSystemException.class);
        exception.expectMessage(CredentialsEntity.class.getName());

        repository.saveAndFlush(entity);
    }

    @Test
    public void testSaveNullUsername() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();
        entity.setUsername(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USERNAME\"");
        }
    }

    @Test
    public void testSaveNullPassword() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();
        entity.setPassword(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"PASSWORD\"");
        }
    }

    @Test
    public void testSaveDuplicateUsername() throws Exception {
        final CredentialsEntity entity1 = getCredentialsEntity();
        final CredentialsEntity entity2 = getCredentialsEntity();
        entity2.setUsername(entity1.getUsername());

        repository.saveAndFlush(entity1);
        try {
            repository.saveAndFlush(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "PUBLIC.CREDENTIALS(USERNAME)");
        }
    }

}
