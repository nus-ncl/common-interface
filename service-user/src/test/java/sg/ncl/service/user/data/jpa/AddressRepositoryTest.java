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
@ContextConfiguration(classes = AddressRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class AddressRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Inject
    private AddressRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGood() throws Exception {
        final AddressEntity entity = TestUtil.getAddressEntity();

        final long count = repository.count();
        final AddressEntity saved = repository.save(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final AddressEntity entity = TestUtil.getAddressEntity();
        entity.setId(null);
        final long count = repository.count();

        final AddressEntity saved = repository.save(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testNullAddress1() throws Exception {
        final AddressEntity entity = TestUtil.getAddressEntity();
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
        final AddressEntity entity = TestUtil.getAddressEntity();
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
        final AddressEntity entity = TestUtil.getAddressEntity();
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
        final AddressEntity entity = TestUtil.getAddressEntity();
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
