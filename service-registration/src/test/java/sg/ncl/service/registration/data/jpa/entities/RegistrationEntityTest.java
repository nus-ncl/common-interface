package sg.ncl.service.registration.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Desmond.
 */
public class RegistrationEntityTest {

    @Test
    public void testGetId() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setId(id);

        assertThat(entity.getId(), is(id));
    }

    @Test
    public void testGetPid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getPid(), is(nullValue()));
    }

    @Test
    public void testSetPid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setPid(id);

        assertThat(entity.getPid(), is(id));
    }

    @Test
    public void testGetUid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUid(), is(nullValue()));
    }

    @Test
    public void testSetUid() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setUid(id);

        assertThat(entity.getUid(), is(id));
    }

    @Test
    public void testGetUserName() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrName(), is(nullValue()));
    }

    @Test
    public void testSetUserName() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String id = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrName(id);

        assertThat(entity.getUsrName(), is(id));
    }

    @Test
    public void testGetUserTitle() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrTitle(), is(nullValue()));
    }

    @Test
    public void testSetUserTitle() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String title = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrTitle(title);

        assertThat(entity.getUsrTitle(), is(title));
    }

    @Test
    public void testGetUserAffiliation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAffil(), is(nullValue()));
    }

    @Test
    public void testSetUserAffiliation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String affiliation = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAffil(affiliation);

        assertThat(entity.getUsrAffil(), is(affiliation));
    }

    @Test
    public void testGetUserAffiliationAbbreviation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAffilAbbrev(), is(nullValue()));
    }

    @Test
    public void testSetUserAffiliationAbbreviation() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String affiliationAbbreviation = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAffilAbbrev(affiliationAbbreviation);

        assertThat(entity.getUsrAffilAbbrev(), is(affiliationAbbreviation));
    }

    @Test
    public void testGetUserEmail() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrEmail(), is(nullValue()));
    }

    @Test
    public void testSetUserEmail() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String email = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrEmail(email);

        assertThat(entity.getUsrEmail(), is(email));
    }

    @Test
    public void testGetUserAddress() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrAddr(), is(nullValue()));
        assertThat(entity.getUsrAddr2(), is(nullValue()));
    }

    @Test
    public void testSetUserAddress() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String address = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAddr(address);

        final String address2 = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrAddr2(address2);

        assertThat(entity.getUsrAddr(), is(address));
        assertThat(entity.getUsrAddr2(), is(address2));
    }

    @Test
    public void testGetUserCity() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrCity(), is(nullValue()));
    }

    @Test
    public void testSetUserCity() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String city = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrCity(city);

        assertThat(entity.getUsrCity(), is(city));
    }

    @Test
    public void testGetUserState() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrState(), is(nullValue()));
    }

    @Test
    public void testSetUserState() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String state = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrState(state);

        assertThat(entity.getUsrState(), is(state));
    }

    @Test
    public void testGetUserZip() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrZip(), is(nullValue()));
    }

    @Test
    public void testSetUserZip() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String zip = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrZip(zip);

        assertThat(entity.getUsrZip(), is(zip));
    }

    @Test
    public void testGetUserCountry() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrCountry(), is(nullValue()));
    }

    @Test
    public void testSetUserCountry() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String country = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrCountry(country);

        assertThat(entity.getUsrCountry(), is(country));
    }

    @Test
    public void testGetUserPhone() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();

        assertThat(entity.getUsrPhone(), is(nullValue()));
    }

    @Test
    public void testSetUserPhone() throws Exception {
        final RegistrationEntity entity = new RegistrationEntity();
        final String phone = RandomStringUtils.randomAlphanumeric(20);
        entity.setUsrPhone(phone);

        assertThat(entity.getUsrPhone(), is(phone));
    }
}
