package sg.ncl.service.registration.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.registration.domain.Registration;

import javax.persistence.*;

/**
 * Created by Desmond and Te Ye on 16-Jun-16.
 */
@Entity
@Table(name = "registrations")
public class RegistrationEntity extends AbstractEntity implements Registration {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "pid", nullable = false)
    private String pid;

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    // actual full name of the user
    // Deterlab restrictions
    // minimum 4 characters
    // must have [firstname]<space>[lastname]
    @Column(name = "usr_name", nullable = false)
    private String usrName;

    @Column(name = "usr_title", nullable = false)
    private String usrTitle;

    @Column(name = "usr_affil", nullable = false)
    private String usrAffil;

    @Column(name = "usr_affil_abbrev", nullable = false)
    private String usrAffilAbbrev;

    @Column(name = "usr_email", nullable = false, unique = true)
    private String usrEmail;

    @Column(name = "usr_addr", nullable = false)
    private String usrAddr;

    @Column(name = "usr_addr2")
    private String usrAddr2;

    @Column(name = "usr_city", nullable = false)
    private String usrCity;

    @Column(name = "usr_state", nullable = false)
    private String usrState;

    @Column(name = "usr_zip", nullable = false)
    private String usrZip;

    @Column(name = "usr_country", nullable = false)
    private String usrCountry;

    @Column(name = "usr_phone", nullable = false)
    private String usrPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    @Override
    public String getUsrTitle() {
        return usrTitle;
    }

    public void setUsrTitle(String usrTitle) {
        this.usrTitle = usrTitle;
    }

    @Override
    public String getUsrAffil() {
        return usrAffil;
    }

    public void setUsrAffil(String usrAffil) {
        this.usrAffil = usrAffil;
    }

    @Override
    public String getUsrAffilAbbrev() {
        return usrAffilAbbrev;
    }

    public void setUsrAffilAbbrev(String usrAffilAbbrev) {
        this.usrAffilAbbrev = usrAffilAbbrev;
    }

    @Override
    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    @Override
    public String getUsrAddr() {
        return usrAddr;
    }

    public void setUsrAddr(String usrAddr) {
        this.usrAddr = usrAddr;
    }

    @Override
    public String getUsrAddr2() {
        return usrAddr2;
    }

    public void setUsrAddr2(String usrAddr2) {
        this.usrAddr2 = usrAddr2;
    }

    @Override
    public String getUsrCity() {
        return usrCity;
    }

    public void setUsrCity(String usrCity) {
        this.usrCity = usrCity;
    }

    @Override
    public String getUsrState() {
        return usrState;
    }

    public void setUsrState(String usrState) {
        this.usrState = usrState;
    }

    @Override
    public String getUsrZip() {
        return usrZip;
    }

    public void setUsrZip(String usrZip) {
        this.usrZip = usrZip;
    }

    @Override
    public String getUsrCountry() {
        return usrCountry;
    }

    public void setUsrCountry(String usrCountry) {
        this.usrCountry = usrCountry;
    }

    @Override
    public String getUsrPhone() {
        return usrPhone;
    }

    public void setUsrPhone(String usrPhone) {
        this.usrPhone = usrPhone;
    }

    /*
    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Registration that = (Registration) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "RegistrationEntity{" +
                "id='" + id + '\'' +
                ", pid=" + pid +
                ", uid=" + uid +
                "} " + super.toString();
    }
    */

}
