package sg.ncl.service.user.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;

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
public class AddressRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Inject
    private AddressRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(AddressRepository.class))));
    }

    @Test
    public void testGood() throws Exception {
        final AddressEntity entity = Util.getAddressEntity();

        final long count = repository.count();
        final AddressEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(Long.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
    }

    @Test
    public void testNullAddress1() throws Exception {
        final AddressEntity entity = Util.getAddressEntity();
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
        final AddressEntity entity = Util.getAddressEntity();
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
    public void testNullCity() throws Exception {
        final AddressEntity entity = Util.getAddressEntity();
        entity.setCity(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"CITY\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullZipCode() throws Exception {
        final AddressEntity entity = Util.getAddressEntity();
        entity.setZipCode(null);
        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ZIP_CODE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

}
