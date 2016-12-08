package sg.ncl.service.user.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 08-Dec-16.
 */
public class AddressInfoTest {

    private final String address1 = RandomStringUtils.randomAlphanumeric(20);
    private final String address2 = RandomStringUtils.randomAlphanumeric(20);
    private final String country = RandomStringUtils.randomAlphanumeric(20);
    private final String region = RandomStringUtils.randomAlphanumeric(20);
    private final String city = RandomStringUtils.randomAlphanumeric(20);
    private final String zipCode = RandomStringUtils.randomAlphanumeric(20);
    private final AddressInfo addressInfo = new AddressInfo(address1, address2, country, region, city, zipCode);

    @Test
    public void testGetAddress1() throws Exception {
        assertThat(addressInfo.getAddress1()).isEqualTo(address1);
    }

    @Test
    public void testGetAddress2() throws Exception {
        assertThat(addressInfo.getAddress2()).isEqualTo(address2);
    }

    @Test
    public void testGetCountry() throws Exception {
        assertThat(addressInfo.getCountry()).isEqualTo(country);
    }

    @Test
    public void testGetRegion() throws Exception {
        assertThat(addressInfo.getRegion()).isEqualTo(region);
    }

    @Test
    public void testGetCity() throws Exception {
        assertThat(addressInfo.getCity()).isEqualTo(city);
    }

    @Test
    public void testGetZipCode() throws Exception {
        assertThat(addressInfo.getZipCode()).isEqualTo(zipCode);
    }
}