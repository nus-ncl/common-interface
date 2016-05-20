package sg.ncl.service.user.data.jpa.entities;

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

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressEntity{");
        sb.append("id=").append(id);
        sb.append(", address1='").append(address1).append('\'');
        sb.append(", address2='").append(address2).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", region='").append(region).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
