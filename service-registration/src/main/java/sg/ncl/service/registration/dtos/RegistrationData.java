package sg.ncl.service.registration.dtos;

import sg.ncl.service.team.data.jpa.entities.TeamEntity;

/**
 * Created by Te Ye on 17-Jun-16.
 */
public class RegistrationData {

    private String pid;
    private String uid;

    // actual full name of the user
    // Deterlab restrictions
    // minimum 4 characters
    // must have [firstname]<space>[lastname]
    private String usrName;
    private String usrTitle;
    private String usrAffil;
    private String usrAffilAbbrev;
    private String usrEmail;
    private String usrAddr;
    private String usrAddr2;
    private String usrCity;
    private String usrState;
    private String usrZip;
    private String usrCountry;
    private String usrPhone;

    private String clearPassword;

    private TeamEntity team;


    public String getPid() {
        return pid;
    }

    void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsrName() {
        return usrName;
    }

    void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrTitle() {
        return usrTitle;
    }

    void setUsrTitle(String usrTitle) {
        this.usrTitle = usrTitle;
    }

    public String getUsrAffil() {
        return usrAffil;
    }

    void setUsrAffil(String usrAffil) {
        this.usrAffil = usrAffil;
    }

    public String getUsrAffilAbbrev() {
        return usrAffilAbbrev;
    }

    void setUsrAffilAbbrev(String usrAffilAbbrev) {
        this.usrAffilAbbrev = usrAffilAbbrev;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUsrAddr() {
        return usrAddr;
    }

    void setUsrAddr(String usrAddr) {
        this.usrAddr = usrAddr;
    }

    public String getUsrAddr2() {
        return usrAddr2;
    }

    void setUsrAddr2(String usrAddr2) {
        this.usrAddr2 = usrAddr2;
    }

    public String getUsrCity() {
        return usrCity;
    }

    void setUsrCity(String usrCity) {
        this.usrCity = usrCity;
    }

    public String getUsrState() {
        return usrState;
    }

    void setUsrState(String usrState) {
        this.usrState = usrState;
    }

    public String getUsrZip() {
        return usrZip;
    }

    void setUsrZip(String usrZip) {
        this.usrZip = usrZip;
    }

    public String getUsrCountry() {
        return usrCountry;
    }

    void setUsrCountry(String usrCountry) {
        this.usrCountry = usrCountry;
    }

    public String getUsrPhone() {
        return usrPhone;
    }

    void setUsrPhone(String usrPhone) {
        this.usrPhone = usrPhone;
    }

    public String getClearPassword() {
        return clearPassword;
    }

    void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }
}
