package sg.ncl.service.authentication.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.authentication.Role;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = CredentialsRepository.class)
public class CredentialsRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private CredentialsRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();

        final long count = repository.count();
        final CredentialsEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(entity.getId()).isEqualTo(saved.getId());
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull();
        assertThat(saved.getVersion()).isEqualTo(1);
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

    @Test
    public void testGoodRole() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();
        CredentialsEntity saved = repository.saveAndFlush(entity);

        assertThat(saved.getRoles()).hasSize(1).containsExactly(Role.USER).doesNotContain(Role.ADMIN);
    }

    @Test
    public void testNullRole() throws Exception {
        final CredentialsEntity entity = getCredentialsEntity();
        entity.addRole(null);
        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ROLE\"");
        }
    }

}
