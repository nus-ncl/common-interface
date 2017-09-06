package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataPublicUserEntityTest {

    @Test
    public void testGetId() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetFullName() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getFullName()).isNull();
    }

    @Test
    public void testSetFullName() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        String name = RandomStringUtils.randomAlphanumeric(10);
        entity.setFullName(name);
        assertThat(entity.getFullName()).isEqualTo(name);
    }

    @Test
    public void testGetEmail() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getEmail()).isNull();
    }

    @Test
    public void testSetEmail() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        String email = RandomStringUtils.randomAlphabetic(10);
        entity.setEmail(email);
        assertThat(entity.getEmail()).isEqualTo(email);
    }

    @Test
    public void testGetJobTitle() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getJobTitle()).isNull();
    }

    @Test
    public void testSetJobTitle() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        String jobTitle = RandomStringUtils.randomAlphanumeric(10);
        entity.setJobTitle(jobTitle);
        assertThat(entity.getJobTitle()).isEqualTo(jobTitle);
    }

    @Test
    public void testGetInstitution() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getInstitution()).isNull();
    }

    @Test
    public void testSetInstitution() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        String institution = RandomStringUtils.randomAlphanumeric(10);
        entity.setInstitution(institution);
        assertThat(entity.getInstitution()).isEqualTo(institution);
    }

    @Test
    public void testGetCountry() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        assertThat(entity.getCountry()).isNull();
    }

    @Test
    public void testSetCountry() {
        DataPublicUserEntity entity = new DataPublicUserEntity();
        String country = RandomStringUtils.randomAlphabetic(10);
        entity.setCountry(country);
        assertThat(entity.getCountry()).isEqualTo(country);
    }

}
