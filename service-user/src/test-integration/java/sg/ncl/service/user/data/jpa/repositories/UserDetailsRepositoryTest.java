package sg.ncl.service.user.data.jpa.repositories;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
        final UserDetailsEntity entity = getUserDetailsEntity();

        final long count = repository.count();
        final UserDetailsEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(Long.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
    }

    @Test
    public void testNullFirstName() throws Exception {
        final UserDetailsEntity entity = getUserDetailsEntity();
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
        final UserDetailsEntity entity = getUserDetailsEntity();
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
    public void testNullAddress() throws Exception {
        final UserDetailsEntity entity = getUserDetailsEntity();
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
        final UserDetailsEntity entity = getUserDetailsEntity();
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
        final UserDetailsEntity entity = getUserDetailsEntity();
        entity.setPhone(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"PHONE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    public static UserDetailsEntity getUserDetailsEntity() {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setFirstName(RandomStringUtils.randomAlphanumeric(20));
        entity.setLastName(RandomStringUtils.randomAlphanumeric(20));
        entity.setAddress(AddressRepositoryTest.getAddressEntity());
        entity.setEmail(RandomStringUtils.randomAlphanumeric(20));
        entity.setPhone(RandomStringUtils.randomAlphanumeric(20));
        return entity;
    }

}
