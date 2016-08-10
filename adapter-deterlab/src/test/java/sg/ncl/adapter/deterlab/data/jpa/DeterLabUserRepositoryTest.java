package sg.ncl.adapter.deterlab.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sg.ncl.adapter.deterlab.AbstractTest;
import sg.ncl.adapter.deterlab.Util;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;

import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Te Ye
 */
public class DeterLabUserRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DeterLabUserRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(DeterLabUserRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final DeterLabUserEntity entity = Util.getDeterlabUserEntity();

        final long count = repository.count();
        final DeterLabUserEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getNclUserId(), is(equalTo(savedEntity.getNclUserId())));
        assertThat(entity.getDeterUserId(), is(equalTo(savedEntity.getDeterUserId())));
    }
}
