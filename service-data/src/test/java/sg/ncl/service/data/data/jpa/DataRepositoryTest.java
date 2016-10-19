package sg.ncl.service.data.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.data.AbstractTest;


import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.data.util.TestUtil.getDataEntity;
import static sg.ncl.service.data.util.TestUtil.getDataEntityWithApprovedUsers;
import static sg.ncl.service.data.util.TestUtil.getDataEntityWithResources;
import static sg.ncl.service.data.util.TestUtil.getDataEntityWithStatistics;

/**
 * Created by dcszwang on 10/6/2016.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class DataRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DataRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull().isInstanceOf(DataRepository.class);

    }

    @Test
    public void testGoodSave() throws Exception {
        final DataEntity entity = getDataEntity();

        final long count = repository.count();
        final DataEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count()).isEqualTo(count+1);
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId()).isInstanceOf(Long.class);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
        assertThat(savedEntity.getVersion()).isEqualTo(0L);
    }

    @Test
    public void testSaveNullName() throws Exception {
        final DataEntity entity = getDataEntity();
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NAME\"");
        }
    }

    @Test
    public void testSaveNullOwnerId() throws Exception {
        final DataEntity entity = getDataEntity();
        entity.setContributorId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"CONTRIBUTOR_ID\"");
        }
    }

    @Test
    public void testSaveNullVisibility() throws Exception {
        final DataEntity entity = getDataEntity();
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
        final DataEntity entity = getDataEntity();
        entity.setAccessibility(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ACCESSIBILITY\"");
        }
    }

    @Test
    public void testSaveResources() throws Exception {
        final DataEntity entity = getDataEntityWithResources();
        final DataEntity savedEntity = repository.saveAndFlush(entity);

        final DataEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getResources().size()).isEqualTo(1);
        assertThat(one.getResources().get(0).getId()).isNotNull();
        assertThat(one.getResources().get(0).getUri()).isEqualTo(entity.getResources().get(0).getUri());
    }

    @Test
    public void testSaveApprovedUsers() throws Exception {
        final DataEntity entity = getDataEntityWithApprovedUsers();
        final DataEntity savedEntity = repository.saveAndFlush(entity);

        final DataEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getApprovedUsers().size()).isEqualTo(1);
        assertThat(one.getApprovedUsers().get(0)).isEqualTo(entity.getApprovedUsers().get(0));
    }

    @Test
    public void testSaveDownloadEntity() throws Exception {
        final DataEntity entity = getDataEntityWithStatistics();
        final DataEntity savedEntity = repository.saveAndFlush(entity);

        final DataEntity one = repository.findOne(savedEntity.getId());

        assertThat(one).isNotNull();
        assertThat(one.getStatistics().size()).isEqualTo(1);
        assertThat(one.getStatistics().get(0).getId()).isNotNull();
        assertThat(one.getStatistics().get(0).getDate()).isEqualTo(entity.getStatistics().get(0).getDate());
        assertThat(one.getStatistics().get(0).getUserId()).isEqualTo(entity.getStatistics().get(0).getUserId());
    }
}
