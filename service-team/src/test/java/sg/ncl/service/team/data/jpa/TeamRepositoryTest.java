package sg.ncl.service.team.data.jpa;

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
import sg.ncl.service.team.util.TestUtil;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * Created by Desmond.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ContextConfiguration(classes = TeamRepository.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class TeamRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private TeamRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(TeamRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final TeamEntity entity = TestUtil.getTeamEntity();

        final long count = repository.count();
        final TeamEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getName(), is(equalTo(savedEntity.getName())));
    }

    @Test
    public void testSaveNullUsername() throws Exception {
        final TeamEntity entity = TestUtil.getTeamEntity();
        entity.setName(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"NAME\"");
        }
    }

    @Test
    public void testSaveNullWebsite() throws Exception {
        final TeamEntity entity = TestUtil.getTeamEntity();
        entity.setWebsite(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"WEBSITE\"");
        }
    }

    @Test
    public void testSaveNullOrganisationType() throws Exception {
        final TeamEntity entity = TestUtil.getTeamEntity();
        entity.setOrganisationType(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ORGANISATION_TYPE\"");
        }
    }

    @Test
    public void testDeleteTeam() throws Exception {
        final TeamEntity entity = TestUtil.getTeamEntity();

        final long count = repository.count();
        final TeamEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getName(), is(equalTo(savedEntity.getName())));

        repository.delete(savedEntity.getId());
        assertThat(repository.count(), is(equalTo(count)));
    }
}
