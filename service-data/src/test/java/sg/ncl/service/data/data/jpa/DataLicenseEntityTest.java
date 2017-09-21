package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataLicenseEntityTest {

    @Test
    public void testGetId() {
        DataLicenseEntity entity = new DataLicenseEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataLicenseEntity entity = new DataLicenseEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetName() {
        DataLicenseEntity entity = new DataLicenseEntity();
        assertThat(entity.getName()).isNull();
    }

    @Test
    public void testSetName() {
        DataLicenseEntity entity = new DataLicenseEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);
        assertThat(entity.getName()).isEqualTo(name);
    }

    @Test
    public void testGetAcronym() {
        DataLicenseEntity entity = new DataLicenseEntity();
        assertThat(entity.getAcronym()).isNull();
    }

    @Test
    public void testSetAcronym() {
        DataLicenseEntity entity = new DataLicenseEntity();
        String acronym = RandomStringUtils.randomAlphanumeric(5);
        entity.setAcronym(acronym);
        assertThat(entity.getAcronym()).isEqualTo(acronym);
    }

    @Test
    public void testGetDescription() {
        DataLicenseEntity entity = new DataLicenseEntity();
        assertThat(entity.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() {
        DataLicenseEntity entity = new DataLicenseEntity();
        String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);
        assertThat(entity.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetLink() {
        DataLicenseEntity entity = new DataLicenseEntity();
        assertThat(entity.getLink()).isNull();
    }

    @Test
    public void testSetLink() {
        DataLicenseEntity entity = new DataLicenseEntity();
        String link = RandomStringUtils.randomAlphanumeric(20);
        entity.setLink(link);
        assertThat(entity.getLink()).isEqualTo(link);
    }

}
