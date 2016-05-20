package sg.ncl.service.user.data.jpa.repositories;

import org.apache.commons.lang3.RandomStringUtils;
import org.h2.jdbc.JdbcSQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 */
public class AddressRepositoryTest extends AbstractTest {

    @Inject
    private AddressRepository repository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(AddressRepository.class))));
    }

    @Test
    public void testGood() throws Exception {
        final AddressEntity entity = getAddressEntity();

        final long count = repository.count();
        final AddressEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(Long.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
    }

    @Test
    public void testNullAddress1() throws Exception {
        final AddressEntity entity = getAddressEntity();
        entity.setAddress1(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ADDRESS_1\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullCountry() throws Exception {
        final AddressEntity entity = getAddressEntity();
        entity.setCountry(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"COUNTRY\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullZipCode() throws Exception {
        final AddressEntity entity = getAddressEntity();
        entity.setZipCode(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ZIP_CODE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    private void checkException(final Exception e, final String message) {
        assertThat(e, is(instanceOf(DataIntegrityViolationException.class)));
        assertThat(e.getCause(), is(instanceOf(ConstraintViolationException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(JdbcSQLException.class)));
        assertThat(e.getCause().getCause().getMessage(), containsString(message));
    }

    public static AddressEntity getAddressEntity() {
        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphanumeric(20));
        address.setAddress2(RandomStringUtils.randomAlphanumeric(20));
        address.setCountry(RandomStringUtils.randomAlphanumeric(20));
        address.setRegion(RandomStringUtils.randomAlphanumeric(20));
        address.setZipCode(RandomStringUtils.randomAlphanumeric(20));
        return address;
    }

}