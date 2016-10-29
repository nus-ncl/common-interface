package sg.ncl.service.image.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.service.image.util.TestUtil;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
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
        final ImageEntity saved = repository.saveAndFlush(entity);

        assertThat(repository.count()).isEqualTo(count + 1);
        assertThat(entity.getId()).isEqualTo(saved.getId());
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull();
        assertThat(saved.getVersion()).isEqualTo(1);
    }
}
