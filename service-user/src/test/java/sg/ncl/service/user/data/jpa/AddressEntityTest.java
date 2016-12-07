package sg.ncl.service.user.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.user.Util;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Christopher Zhong
 */
public class AddressEntityTest {

    @Test
    public void testGetId() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getId()).isNull();
    }

    @Test
    public void testSetId() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final Long id = new Random().nextLong();
        addressEntity.setId(id);

        assertThat(addressEntity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetAddress1() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getAddress1()).isNull();
    }

    @Test
    public void testSetAddress1() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String address1 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress1(address1);

        assertThat(addressEntity.getAddress1()).isEqualTo(address1);
    }

    @Test
    public void testGetAddress2() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getAddress2()).isNull();
    }

    @Test
    public void testSetAddress2() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String address2 = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setAddress2(address2);

        assertThat(addressEntity.getAddress2()).isEqualTo(address2);
    }

    @Test
    public void testGetCountry() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getCountry()).isNull();
    }

    @Test
    public void testSetCountry() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String country = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCountry(country);

        assertThat(addressEntity.getCountry()).isEqualTo(country);
    }

    @Test
    public void testGetRegion() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getRegion()).isNull();
    }

    @Test
    public void testSetRegion() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String region = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setRegion(region);

        assertThat(addressEntity.getRegion()).isEqualTo(region);
    }

    @Test
    public void testGetCity() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getCity()).isNull();
    }

    @Test
    public void testSetCity() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String city = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setCity(city);

        assertThat(addressEntity.getCity()).isEqualTo(city);
    }

    @Test
    public void testGetZipCode() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();

        assertThat(addressEntity.getZipCode()).isNull();
    }

    @Test
    public void testSetZipCode() throws Exception {
        final AddressEntity addressEntity = new AddressEntity();
        final String zipCode = RandomStringUtils.randomAlphanumeric(20);
        addressEntity.setZipCode(zipCode);

        assertThat(addressEntity.getZipCode()).isEqualTo(zipCode);
    }

    @Test
    public void testStaticGetAddress() {
        final AddressEntity addressEntity = Util.getAddressEntity();
        AddressEntity returnAddressEntity = AddressEntity.get(addressEntity);

        assertThat(returnAddressEntity.getAddress1()).isEqualTo(addressEntity.getAddress1());
        assertThat(returnAddressEntity.getAddress2()).isEqualTo(addressEntity.getAddress2());
        assertThat(returnAddressEntity.getCountry()).isEqualTo(addressEntity.getCountry());
        assertThat(returnAddressEntity.getRegion()).isEqualTo(addressEntity.getRegion());
        assertThat(returnAddressEntity.getCity()).isEqualTo(addressEntity.getCity());
        assertThat(returnAddressEntity.getZipCode()).isEqualTo(addressEntity.getZipCode());
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

        assertThat(toString).contains(id.toString());
        assertThat(toString).contains(address1);
        assertThat(toString).contains(address2);
        assertThat(toString).contains(country);
        assertThat(toString).contains(region);
        assertThat(toString).contains(city);
        assertThat(toString).contains(zipCode);
    }

}
