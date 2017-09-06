package sg.ncl.service.data.data.jpa;

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
import static sg.ncl.service.data.util.TestUtil.getDataLicenseEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = DataLicenseRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class DataLicenseRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DataLicenseRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull().isInstanceOf(DataLicenseRepository.class);
    }

    @Test
    public void testGoodSave() throws Exception {
        final DataLicenseEntity entity = getDataLicenseEntity();
        final long count = repository.count();
        final DataLicenseEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count()).isEqualTo(count+1);
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId()).isInstanceOf(Long.class);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
        assertThat(savedEntity.getVersion()).isEqualTo(0L);
    }


    @Test
    public void testSaveNullName() throws Exception {
        final DataLicenseEntity entity = getDataLicenseEntity();
        entity.setName(null);
        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NAME\"");
        }
    }

}
