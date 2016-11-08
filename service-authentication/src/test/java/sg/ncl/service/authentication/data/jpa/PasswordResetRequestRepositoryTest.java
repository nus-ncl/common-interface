package sg.ncl.service.authentication.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.authentication.util.TestUtil.getPasswordResetRequestEntity;

/**
 * Created by dcszwang on 11/8/2016.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = PasswordResetRequestRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class PasswordResetRequestRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private PasswordResetRequestRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final PasswordResetRequestEntity entity = getPasswordResetRequestEntity();

        final long count = repository.count();
        final PasswordResetRequestEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(entity.getId()).isNotNull();
        assertThat(saved.getHash()).isEqualTo(entity.getHash());
        assertThat(saved.getUsername()).isEqualTo(entity.getUsername());
        assertThat(saved.getTime()).isEqualTo(entity.getTime());
    }

    @Test
    public void testSaveNullUsername() throws Exception {
        final PasswordResetRequestEntity entity = getPasswordResetRequestEntity();
        entity.setUsername(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USERNAME\"");
        }
    }

    @Test
    public void testSaveNullHash() throws Exception {
        final PasswordResetRequestEntity entity = getPasswordResetRequestEntity();
        entity.setHash(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"HASH\"");
        }
    }


    @Test
    public void testSaveNullTime() throws Exception {
        final PasswordResetRequestEntity entity = getPasswordResetRequestEntity();
        entity.setTime(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"TIME\"");
        }
    }

    @Test
    public void testSaveDuplicateHash() throws Exception {
        final PasswordResetRequestEntity entity1 = getPasswordResetRequestEntity();
        final PasswordResetRequestEntity entity2 = getPasswordResetRequestEntity();
        entity2.setHash(entity1.getHash());

        repository.saveAndFlush(entity1);
        try {
            repository.saveAndFlush(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "PUBLIC.PASSWORD_RESET_REQUESTS(HASH)");
        }
    }
}
