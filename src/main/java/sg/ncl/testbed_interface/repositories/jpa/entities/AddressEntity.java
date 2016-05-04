package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.Address;

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
    private Long id = null;

    @Column(name = "address_1", nullable = false)
    private String address1 = null;

    @Column(name = "address_2")
    private String address2 = null;

    @Column(name = "country", nullable = false)
    private String country = null;

    @Column(name = "region")
    private String region = null;

    @Column(name = "zip_code", nullable = false)
    private String zipCode = null;

    public Long getId() {
        return id;
    }

    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    void setAddress1(final String address1) {
        this.address1 = address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    void setAddress2(final String address2) {
        this.address2 = address2;
    }

    @Override
    public String getCountry() {
        return country;
    }

    void setCountry(final String country) {
        this.country = country;
    }

    @Override
    public String getRegion() {
        return region;
    }

    void setRegion(final String region) {
        this.region = region;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    void setZipCode(final String zipCode) {
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
