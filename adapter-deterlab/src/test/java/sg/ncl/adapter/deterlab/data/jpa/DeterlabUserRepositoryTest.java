package sg.ncl.adapter.deterlab.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sg.ncl.adapter.deterlab.AbstractTest;
import sg.ncl.adapter.deterlab.Util;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;


import javax.inject.Inject;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Te Ye
 */
public class DeterlabUserRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private DeterlabUserRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(DeterlabUserRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final DeterlabUserEntity entity = Util.getDeterlabUserEntity();

        final long count = repository.count();
        final DeterlabUserEntity savedEntity = repository.save(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(entity.getNclUserId(), is(equalTo(savedEntity.getNclUserId())));
        assertThat(entity.getDeterUserId(), is(equalTo(savedEntity.getDeterUserId())));
    }
}
