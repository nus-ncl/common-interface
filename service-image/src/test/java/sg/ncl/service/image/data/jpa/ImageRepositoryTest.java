package sg.ncl.service.image.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.image.util.TestUtil.getImageEntity;

/**
 * Created by dcsyeoty on 29-Oct-16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = ImageRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class ImageRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private ImageRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testGoodSave() throws Exception {
        final ImageEntity entity = getImageEntity();

        final long count = repository.count();
        final ImageEntity savedEntity = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(savedEntity.getCreatedDate()).isNotNull();
        assertThat(savedEntity.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testSaveWithNullId() throws Exception {
        final ImageEntity entity = getImageEntity();
        entity.setId(null);
        final long count = repository.count();

        final ImageEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingEntityWithNullId() throws Exception {
        final ImageEntity entity = getImageEntity();
        final ImageEntity saved = repository.saveAndFlush(entity);
        saved.setId(null);

        exception.expect(JpaSystemException.class);

        repository.saveAndFlush(saved);
    }

    @Test
    public void testSaveWithNullTeamId() throws Exception {
        final ImageEntity entity = getImageEntity();
        entity.setTeamId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"TEAM_ID\"");
        }
    }

    @Test
    public void testSaveWithNullNodeId() throws Exception {
        final ImageEntity entity = getImageEntity();
        entity.setNodeId(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NODE_ID\"");
        }
    }

    @Test
    public void testSaveWithNullImageName() throws Exception {
        final ImageEntity entity = getImageEntity();
        entity.setImageName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"IMAGE_NAME\"");
        }
    }

    @Test
    public void testSaveWithNullVisibility() throws Exception {
        final ImageEntity entity = getImageEntity();
        entity.setVisibility(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"VISIBILITY\"");
        }
    }
}
