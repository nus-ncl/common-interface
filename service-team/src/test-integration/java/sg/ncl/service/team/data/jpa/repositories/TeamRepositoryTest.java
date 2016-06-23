package sg.ncl.service.team.data.jpa.repositories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * Created by Desmond.
 */
public class TeamRepositoryTest extends AbstractTest {

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
        final TeamEntity entity = Util.getTeamEntity();

        final long count = repository.count();
        final TeamEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getName(), is(equalTo(savedEntity.getName())));
    }

    @Test
    public void testSaveNullUsername() throws Exception {
        final TeamEntity entity = Util.getTeamEntity();
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
        final TeamEntity entity = Util.getTeamEntity();
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
        final TeamEntity entity = Util.getTeamEntity();
        entity.setOrganisationType(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ORGANISATION_TYPE\"");
        }
    }
}