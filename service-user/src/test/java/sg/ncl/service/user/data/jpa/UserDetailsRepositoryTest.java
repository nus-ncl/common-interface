package sg.ncl.service.user.data.jpa;

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
import sg.ncl.service.user.Util;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = UserDetailsRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class UserDetailsRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Inject
    private UserDetailsRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGood() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();

        final long count = repository.count();
        final UserDetailsEntity saved = repository.save(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testNullFirstName() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setFirstName(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"FIRST_NAME\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullLastName() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setLastName(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"LAST_NAME\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullJobTitle() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setJobTitle(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"JOB_TITLE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullAddress() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setAddress(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ADDRESS_ID\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullEmail() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setEmail(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"EMAIL\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullPhone() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setPhone(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"PHONE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullInstitution() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setInstitution(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"INSTITUTION\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullInstitutionAbbreviation() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setInstitutionAbbreviation(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"INSTITUTION_ABBREVIATION\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullInstitutionWeb() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();
        entity.setInstitutionWeb(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"INSTITUTION_WEB\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

}
