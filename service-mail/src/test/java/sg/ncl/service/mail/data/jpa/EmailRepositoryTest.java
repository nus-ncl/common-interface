package sg.ncl.service.mail.data.jpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.mail.AbstractTest;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static sg.ncl.common.test.Checks.checkException;
import static sg.ncl.service.mail.util.TestUtil.getEmailEntity;

/**
 * Created by dcszwang on 9/6/2016.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class EmailRepositoryTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private EmailRepository repository;

    @Test
    public void testRepositoryExists() throws Exception {
        assertThat(repository, is(not(nullValue(EmailRepository.class))));
    }

    @Test
    public void testGoodSave() throws Exception {
        final EmailEntity entity = getEmailEntity();

        final long count = repository.count();
        final EmailEntity savedEntity = repository.saveAndFlush(entity);
        assertThat(repository.count(), is(equalTo(count + 1)));
        assertThat(savedEntity.getId(), is(not(nullValue(Long.class))));
        assertThat(savedEntity.getCreatedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getLastModifiedDate(), is(not(nullValue(ZonedDateTime.class))));
        assertThat(savedEntity.getVersion(), is(equalTo(0L)));
    }

    @Test
    public void testSaveNullSender() throws Exception {
        final EmailEntity entity = getEmailEntity();
        entity.setSender(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"SENDER\"");
        }
    }

    @Test
    public void testSaveNullRecipients() throws Exception {
        final EmailEntity entity = getEmailEntity();
        entity.setRecipients(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"RECIPIENTS\"");
        }
    }

    @Test
    public void testSaveNullSubject() throws Exception {
        final EmailEntity entity = getEmailEntity();
        entity.setSubject(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"SUBJECT\"");
        }
    }

    @Test
    public void testSaveNullContent() throws Exception {
        final EmailEntity entity = getEmailEntity();
        entity.setContent(null);

        try {
            repository.saveAndFlush(entity);
            exception.expect(DataIntegrityViolationException.class);
        } catch (Exception e) {
            checkException(e, "NULL not allowed for column \"CONTENT\"");
        }
    }
}
