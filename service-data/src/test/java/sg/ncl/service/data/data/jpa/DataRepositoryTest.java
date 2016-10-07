package sg.ncl.service.data.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.data.AbstractTest;


import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.data.util.TestUtil.getDatasetEntity;
import static sg.ncl.service.data.util.TestUtil.getDatasetEntityWithApprovedUsers;
import static sg.ncl.service.data.util.TestUtil.getDatasetEntityWithDownloadHistory;
import static sg.ncl.service.data.util.TestUtil.getDatasetEntityWithResources;

/**
 * Created by dcszwang on 10/6/2016.
 */
public class DataRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DatasetRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull().isInstanceOf(DatasetRepository.class);

    }

    @Test
    public void testGoodSave() throws Exception {
        final DatasetEntity entity = getDatasetEntity();

        final long count = repository.count();
        final DatasetEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count()).isEqualTo(count+1);
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId()).isInstanceOf(String.class);
        assertThat(savedEntity.getId().length()).isNotZero();
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
        assertThat(savedEntity.getVersion()).isEqualTo(0L);
    }

    @Test
    public void testSaveNullName() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NAME\"");
        }
    }

    @Test
    public void testSaveNullDescription() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setDescription(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"DESCRIPTION\"");
        }
    }

    @Test
    public void testSaveNullOwnerId() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setOwnerId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"OWNER_ID\"");
        }
    }

    @Test
    public void testSaveNullVisibility() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setVisibility(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"VISIBILITY\"");
        }
    }

    @Test
    public void testSaveNullAccessibility() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setAccessibility(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ACCESSIBILITY\"");
        }
    }

    @Test
    public void testSaveNullStatus() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setStatus(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"STATUS\"");
        }
    }

    @Test
    public void testSaveNullSize() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setSize(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"SIZE\"");
        }
    }

    @Test
    public void testSaveInvalidSize() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setSize(-1L);

        try {
            repository.saveAndFlush(entity);
            exception.expect(ConstraintViolationException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ConstraintViolationException.class);
            assertThat(e.getMessage()).contains("must be greater than or equal to 0");
        }
    }

    @Test
    public void testSaveInvalidDownloadTimes() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setDownloadTimes(-1);

        try {
            repository.saveAndFlush(entity);
            exception.expect(ConstraintViolationException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ConstraintViolationException.class);
            assertThat(e.getMessage()).contains("must be greater than or equal to 0");
        }
    }

    @Test
    public void testSaveNullCategory() throws Exception {
        final DatasetEntity entity = getDatasetEntity();
        entity.setCategory(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"CATEGORY\"");
        }
    }

    @Test
    public void testSaveResources() throws Exception {
        final DatasetEntity entity = getDatasetEntityWithResources();
        final DatasetEntity savedEntity = repository.saveAndFlush(entity);

        final DatasetEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getResources().size()).isEqualTo(1);
        assertThat(one.getResources().get(0).getId()).isNotNull();
        assertThat(one.getResources().get(0).getType()).isEqualTo(entity.getResources().get(0).getType());
        assertThat(one.getResources().get(0).getLink()).isEqualTo(entity.getResources().get(0).getLink());
    }

    @Test
    public void testSaveApprovedUsers() throws Exception {
        final DatasetEntity entity = getDatasetEntityWithApprovedUsers();
        final DatasetEntity savedEntity = repository.saveAndFlush(entity);

        final DatasetEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getApprovedUsers().size()).isEqualTo(1);
        assertThat(one.getApprovedUsers().get(0)).isEqualTo(entity.getApprovedUsers().get(0));
    }

    @Test
    public void testSaveDownloadEntity() throws Exception {
        final DatasetEntity entity = getDatasetEntityWithDownloadHistory();
        final DatasetEntity savedEntity = repository.saveAndFlush(entity);

        final DatasetEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getDownloadHistory().size()).isEqualTo(1);
        assertThat(one.getDownloadHistory().get(0).getId()).isNotNull();
        assertThat(one.getDownloadHistory().get(0).getDate()).isEqualTo(entity.getDownloadHistory().get(0).getDate());
        assertThat(one.getDownloadHistory().get(0).getUserId()).isEqualTo(entity.getDownloadHistory().get(0).getUserId());
        assertThat(one.getDownloadHistory().get(0).isSuccess()).isEqualTo(entity.getDownloadHistory().get(0).isSuccess());
    }
}
