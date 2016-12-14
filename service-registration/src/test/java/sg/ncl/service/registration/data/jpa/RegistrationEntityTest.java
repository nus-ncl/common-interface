package sg.ncl.service.registration.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Desmond.
 */
public class RegistrationEntityTest {

    @Test
    public void testGetId() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final Long id = new Random().nextLong();
        entity.setId(id);

        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetPid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getPid()).isNull();
    }

    @Test
    public void testSetPid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setPid(id);

        assertThat(entity.getPid()).isEqualTo(id);
    }

    @Test
    public void testGetUid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUid()).isNull();
    }

    @Test
    public void testSetUid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setUid(id);

        assertThat(entity.getUid()).isEqualTo(id);
    }

    @Test
    public void testGetUserName() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrName()).isNull();
    }

    @Test
    public void testSetUserName() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrName(id);

        assertThat(entity.getUsrName()).isEqualTo(id);
    }

    @Test
    public void testGetUserTitle() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrTitle()).isNull();
    }

    @Test
    public void testSetUserTitle() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String title = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrTitle(title);

        assertThat(entity.getUsrTitle()).isEqualTo(title);
    }

    @Test
    public void testGetUserAffiliation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAffil()).isNull();
    }

    @Test
    public void testSetUserAffiliation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String affiliation = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAffil(affiliation);

        assertThat(entity.getUsrAffil()).isEqualTo(affiliation);
    }

    @Test
    public void testGetUserAffiliationAbbreviation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAffilAbbrev()).isNull();
    }

    @Test
    public void testSetUserAffiliationAbbreviation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String affiliationAbbreviation = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAffilAbbrev(affiliationAbbreviation);

        assertThat(entity.getUsrAffilAbbrev()).isEqualTo(affiliationAbbreviation);
    }

    @Test
    public void testGetUserEmail() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrEmail()).isNull();
    }

    @Test
    public void testSetUserEmail() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String email = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrEmail(email);

        assertThat(entity.getUsrEmail()).isEqualTo(email);
    }

    @Test
    public void testGetUserAddress() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAddr()).isNull();
        assertThat(entity.getUsrAddr2()).isNull();
    }

    @Test
    public void testSetUserAddress() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String address = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAddr(address);

        final String address2 = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAddr2(address2);

        assertThat(entity.getUsrAddr()).isEqualTo(address);
        assertThat(entity.getUsrAddr2()).isEqualTo(address2);
    }

    @Test
    public void testGetUserCity() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrCity()).isNull();
    }

    @Test
    public void testSetUserCity() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String city = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrCity(city);

        assertThat(entity.getUsrCity()).isEqualTo(city);
    }

    @Test
    public void testGetUserState() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrState()).isNull();
    }

    @Test
    public void testSetUserState() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String state = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrState(state);

        assertThat(entity.getUsrState()).isEqualTo(state);
    }

    @Test
    public void testGetUserZip() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrZip()).isNull();
    }

    @Test
    public void testSetUserZip() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String zip = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrZip(zip);

        assertThat(entity.getUsrZip()).isEqualTo(zip);
    }

    @Test
    public void testGetUserCountry() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrCountry()).isNull();
    }

    @Test
    public void testSetUserCountry() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String country = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrCountry(country);

        assertThat(entity.getUsrCountry()).isEqualTo(country);
    }

    @Test
    public void testGetUserPhone() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrPhone()).isNull();
    }

    @Test
    public void testSetUserPhone() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String phone = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrPhone(phone);

        assertThat(entity.getUsrPhone()).isEqualTo(phone);
    }
}
