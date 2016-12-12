package sg.ncl.service.user.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.user.domain.Address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class AddressEntity extends AbstractEntity implements Address {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "address_1", nullable = false)
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    public static AddressEntity get(final Address address) {
        final AddressEntity entity = new AddressEntity();
        entity.setAddress1(address.getAddress1());
        entity.setAddress2(address.getAddress2());
        entity.setCountry(address.getCountry());
        entity.setRegion(address.getRegion());
        entity.setCity(address.getCity());
        entity.setZipCode(address.getZipCode());
        return entity;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                "} " + super.toString();
    }

}
