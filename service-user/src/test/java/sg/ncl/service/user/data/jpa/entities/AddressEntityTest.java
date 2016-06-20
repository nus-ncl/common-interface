package sg.ncl.service.user.data.jpa.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Christopher Zhong
 */
public class AddressEntityTest {

    @Test
    public void testGetId() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getId(), is(nullValue()));
    }

    @Test
    public void testSetId() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final Long id = new Random().nextLong();
        addressEntity.setId(id);

        assertThat(addressEntity.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetAddress1() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getAddress1(), is(nullValue()));
    }

    @Test
    public void testSetAddress1() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String address1 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress1(address1);

        assertThat(addressEntity.getAddress1(), is(equalTo(address1)));
    }

    @Test
    public void testGetAddress2() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getAddress2(), is(nullValue()));
    }

    @Test
    public void testSetAddress2() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String address2 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress2(address2);

        assertThat(addressEntity.getAddress2(), is(equalTo(address2)));
    }

    @Test
    public void testGetCountry() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getCountry(), is(nullValue()));
    }

    @Test
    public void testSetCountry() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String country = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCountry(country);

        assertThat(addressEntity.getCountry(), is(equalTo(country)));
    }

    @Test
    public void testGetRegion() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getRegion(), is(nullValue()));
    }

    @Test
    public void testSetRegion() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String region = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setRegion(region);

        assertThat(addressEntity.getRegion(), is(equalTo(region)));
    }

    @Test
    public void testGetCity() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getCity(), is(nullValue()));
    }

    @Test
    public void testSetCity() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String city = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCity(city);

        assertThat(addressEntity.getCity(), is(equalTo(city)));
    }

    @Test
    public void testGetZipCode() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getZipCode(), is(nullValue()));
    }

    @Test
    public void testSetZipCode() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String zipCode = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setZipCode(zipCode);

        assertThat(addressEntity.getZipCode(), is(equalTo(zipCode)));
    }

    @Test
    public void testToString() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final Long id = new Random().nextLong();
        addressEntity.setId(id);
        final String address1 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress1(address1);
        final String address2 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress2(address2);
        final String country = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCountry(country);
        final String region = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setRegion(region);
        final String city = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCity(city);
        final String zipCode = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setZipCode(zipCode);

        final String toString = addressEntity.toString();

        assertThat(toString, containsString(id.toString()));
        assertThat(toString, containsString(address1));
        assertThat(toString, containsString(address2));
        assertThat(toString, containsString(country));
        assertThat(toString, containsString(region));
        assertThat(toString, containsString(city));
        assertThat(toString, containsString(zipCode));
    }

}
