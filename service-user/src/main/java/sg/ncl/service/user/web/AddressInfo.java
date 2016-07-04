package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public AddressInfo(@JsonProperty("address1") final String address1, @JsonProperty("address2") final String address2, @JsonProperty("country") final String country, @JsonProperty("region") final String region, @JsonProperty("city") final String city, @JsonProperty("zipCode") final String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.region = region;
        this.city = city;
        this.zipCode = zipCode;
    }

    public AddressInfo(Address address) {
        this(address.getAddress1(),
                address.getAddress2(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getZipCode()
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