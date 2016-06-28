package sg.ncl.service.user.web;

import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.domain.Address;

/**
 * @author Christopher Zhong
 */
public class AddressInfo implements Address {

    private final String address1;
    private final String address2;
    private final String country;
    private final String region;
    private final String city;
    private final String zipCode;

    public AddressInfo(final String address1, final String address2, final String country, final String region, final String city, final String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.region = region;
        this.city = city;
        this.zipCode = zipCode;
    }

    public AddressInfo(AddressEntity addressEntity) {
        this(addressEntity.getAddress1(),
                addressEntity.getAddress2(),
                addressEntity.getCountry(),
                addressEntity.getRegion(),
                addressEntity.getCity(),
                addressEntity.getZipCode()
        );
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
    public String getCity() { return city; }

    @Override
    public String getZipCode() {
        return zipCode;
    }
}
