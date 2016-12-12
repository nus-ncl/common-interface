package sg.ncl.service.user.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.service.user.util.TestUtil;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = UserRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class UserRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Inject
    private UserRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGood() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();

        final long count = repository.count();
        final UserEntity persistedEntity = repository.save(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(persistedEntity.getCreatedDate()).isNotNull();
        assertThat(persistedEntity.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setId(null);
        final long count = repository.count();

        final UserEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingEntityWithNullId() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        final UserEntity saved = repository.saveAndFlush(entity);
        saved.setId(null);

        exception.expect(JpaSystemException.class);

        repository.saveAndFlush(saved);
    }

    @Test
    public void testNullUserDetails() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setUserDetails(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_DETAILS_ID\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullApplicationDate() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setApplicationDate(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"APPLICATION_DATE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullUserDetailsEntity() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setUserDetails(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_DETAILS_ID\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullUserStatus() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setStatus(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"STATUS\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }
}
