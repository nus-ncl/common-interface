package sg.ncl.service.user.data.jpa.repositories;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.data.jpa.entities.UserEntity;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;

/**
 * @author Christopher Zhong
 */
public class UserRepositoryTest extends AbstractTest {

    @Inject
    private UserRepository repository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(UserRepository.class))));
    }

    @Test
    public void testGood() throws Exception {
        final UserEntity entity = getUserEntity();

        final long count = repository.count();
        final UserEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(String.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
    }

    @Test
    @Ignore("not null is not enforced")
    public void testNullUserDetails() throws Exception {
        final UserEntity entity = getUserEntity();
        entity.setUserDetails(null);

        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_DETAILS_ID\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    @Ignore("not null is not enforced")
    public void testNullApplicationDate() throws Exception {
        final UserEntity entity = getUserEntity();
        entity.setApplicationDate(null);

        try {
            repository.save(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"APPLICATION_DATE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    public static UserEntity getUserEntity() {
        final UserEntity entity = new UserEntity();
        entity.setUserDetails(UserDetailsRepositoryTest.getUserDetailsEntity());
        final ZonedDateTime applicationDate = ZonedDateTime.now();
        entity.setApplicationDate(applicationDate);
        final ZonedDateTime processedDate = applicationDate.plusDays(10);
        entity.setProcessedDate(processedDate);
        return entity;
    }

}
