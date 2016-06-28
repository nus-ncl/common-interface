package sg.ncl.service.user.data.jpa.repositories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.data.jpa.UserDetailsRepository;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * @author Christopher Zhong
 */
public class UserDetailsRepositoryTest extends AbstractTest {

    @Inject
    private UserDetailsRepository repository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(UserDetailsRepository.class))));
    }

    @Test
    public void testGood() throws Exception {
        final UserDetailsEntity entity = Util.getUserDetailsEntity();

        final long count = repository.count();
        final UserDetailsEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(Long.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
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
