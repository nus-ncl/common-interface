package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.user.domain.Address;

/**
 * @author Christopher Zhong
 */
@Getter
public class AddressInfo implements Address {

    private final String address1;
    private final String address2;
    private final String country;
    private final String region;
    private final String city;
    private final String zipCode;

    @JsonCreator
    public AddressInfo(
            @JsonProperty("address1") final String address1,
            @JsonProperty("address2") final String address2,
            @JsonProperty("country") final String country,
            @JsonProperty("region") final String region,
            @JsonProperty("city") final String city,
            @JsonProperty("zipCode") final String zipCode
    ) {
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.region = region;
        this.city = city;
        this.zipCode = zipCode;
    }

    public AddressInfo(Address address) {
        this(
                address.getAddress1(),
                address.getAddress2(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getZipCode()
        );
    }

}
