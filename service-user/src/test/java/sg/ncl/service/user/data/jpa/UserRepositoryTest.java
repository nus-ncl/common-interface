package sg.ncl.service.user.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.domain.User;

import javax.inject.Inject;

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
        final UserEntity entity = Util.getUserEntity();

        final long count = repository.count();
        final UserEntity persistedEntity = repository.save(entity);
        assertThat(persistedEntity.getId(), is(not(nullValue(String.class))));
        assertThat(repository.count(), is(equalTo(count + 1)));
    }

    @Test
    public void testNullUserDetails() throws Exception {
        final UserEntity entity = Util.getUserEntity();
        entity.setUserDetails(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"USER_DETAILS_ID\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testNullApplicationDate() throws Exception {
        final UserEntity entity = Util.getUserEntity();
        entity.setApplicationDate(null);

        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"APPLICATION_DATE\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

    @Test
    public void testGoodRole() throws Exception {
        final UserEntity entity = Util.getUserEntity();
        UserEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(savedEntity.getRoles().size(), is(1));
        assertThat(savedEntity.getRoles().contains(User.Role.USER), is(true));
        assertThat(savedEntity.getRoles().contains(User.Role.ADMIN), is(false));
    }

    @Test
    public void testNullRole() throws Exception {
        final UserEntity entity = Util.getUserEntity();
        entity.addRole(null);
        try {
            repository.saveAndFlush(entity);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"ROLES\"");
            return;
        }
        exception.expect(DataIntegrityViolationException.class);
    }

}
