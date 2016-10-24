package sg.ncl.service.registration.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * Created by Te Ye on 17-Jun-16.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class RegistrationRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private RegistrationRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(RegistrationRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();

        final long count = repository.count();
        final RegistrationEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getUid(), is(equalTo(savedEntity.getUid())));
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSaveNullDeterProjectId() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setPid(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"PID\"");
        }
    }

    @Test
    public void testSaveNullDeterUserTitle() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrTitle(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_TITLE\"");
        }
    }

    @Test
    public void testSaveNullDeterUserAffil() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrAffil(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_AFFIL\"");
        }
    }

    @Test
    public void testSaveNullDeterUserAffilAbbrev() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrAffilAbbrev(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_AFFIL_ABBREV\"");
        }
    }

    @Test
    public void testSaveNullDeterUserId() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUid(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"UID\"");
        }
    }

    @Test
    public void testSaveNUllDeterUserEmail() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrEmail(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_EMAIL\"");
        }
    }

    @Test
    public void testSaveNullDeterUserAddr() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrAddr(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_ADDR\"");
        }
    }

    @Test
    public void testSaveNullDeterUserCity() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrCity(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_CITY\"");
        }
    }

    @Test
    public void testSaveNullDeterUserState() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrState(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_STATE\"");
        }
    }

    @Test
    public void testSaveNullDeterUserZip() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrZip(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_ZIP\"");
        }
    }

    @Test
    public void testSaveNullDeterUserCountry() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrCountry(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_COUNTRY\"");
        }
    }

    @Test
    public void testSaveNullDeterUserPhone() throws Exception {
        final RegistrationEntity entity = Util.getRegistrationEntity();
        entity.setUsrPhone(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USR_PHONE\"");
        }
    }

    @Test
    public void testSaveDuplicateDeterUserId() throws Exception {
        final RegistrationEntity entity1 = Util.getRegistrationEntity();
        final RegistrationEntity entity2 = Util.getRegistrationEntity();
        entity2.setUid(entity1.getUid());

        repository.saveAndFlush(entity1);
        try {
            repository.saveAndFlush(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "PUBLIC.REGISTRATIONS(UID)");
        }
    }

    @Test
    public void testSaveDuplicateDeterUserEmail() throws Exception {
        final RegistrationEntity entity1 = Util.getRegistrationEntity();
        final RegistrationEntity entity2 = Util.getRegistrationEntity();
        entity2.setUsrEmail(entity1.getUsrEmail());

        repository.saveAndFlush(entity1);
        try {
            repository.saveAndFlush(entity2);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "PUBLIC.REGISTRATIONS(USR_EMAIL)");
        }
    }

}
