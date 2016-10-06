package sg.ncl.service.data.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sg.ncl.service.data.AbstractTest;


import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.service.data.util.TestUtil.getDatasetEntity;

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
}
