package sg.ncl.service.user.dtos;

import sg.ncl.service.user.domain.Address;

/**
 * @author Christopher Zhong
 */
public class AddressInfo implements Address {

    private final String address1;
    private final String address2;
    private final String country;
    private final String region;
    private final String zipCode;

    public AddressInfo(final String address1, final String address2, final String country, final String region, final String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.region = region;
        this.zipCode = zipCode;
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }
}
