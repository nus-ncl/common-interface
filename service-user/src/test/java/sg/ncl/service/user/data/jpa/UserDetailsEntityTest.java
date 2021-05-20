package sg.ncl.service.user.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.user.util.TestUtil;
import sg.ncl.service.user.domain.UserDetails;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tran Ly Vu
 * @Version 1.0
 */
public class UserDetailsEntityTest {

    @Test
    public void testGet(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        UserDetailsEntity userDetails= new UserDetailsEntity();

        final String randomTestFirstName = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestLastName = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestJobTitle = RandomStringUtils.randomAlphanumeric(20);
        final AddressEntity randomTestAddress = TestUtil.getAddressEntity();
        final String randomTestEmail = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestPhone = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestInstitution = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestInstitutionAbbreviation = RandomStringUtils.randomAlphanumeric(20);
        final String randomTestInstitutionWeb = RandomStringUtils.randomAlphanumeric(20);

        userDetails.setFirstName(randomTestFirstName);
        userDetails.setLastName(randomTestLastName);
        userDetails.setJobTitle(randomTestJobTitle);
        userDetails.setAddress(randomTestAddress);
        userDetails.setEmail(randomTestEmail);
        userDetails.setPhone(randomTestPhone);
        userDetails.setInstitution(randomTestInstitution);
        userDetails.setInstitutionAbbreviation(randomTestInstitutionAbbreviation);
        userDetails.setInstitutionWeb(randomTestInstitutionWeb);

        UserDetailsEntity actual=userDetailsEntity.get((UserDetails)userDetails);

        assertThat(actual.getFirstName()).isEqualTo(randomTestFirstName);
        assertThat(actual.getLastName()).isEqualTo(randomTestLastName);
        assertThat(actual.getJobTitle()).isEqualTo(randomTestJobTitle);
        assertThat(actual.getAddress().getAddress1()).isEqualTo(randomTestAddress.getAddress1());
        assertThat(actual.getAddress().getAddress2()).isEqualTo(randomTestAddress.getAddress2());
        assertThat(actual.getAddress().getCountry()).isEqualTo(randomTestAddress.getCountry());
        assertThat(actual.getAddress().getCity()).isEqualTo(randomTestAddress.getCity());
        assertThat(actual.getAddress().getRegion()).isEqualTo(randomTestAddress.getRegion());
        assertThat(actual.getAddress().getZipCode()).isEqualTo(randomTestAddress.getZipCode());
        assertThat(actual.getEmail()).isEqualTo(randomTestEmail);
        assertThat(actual.getPhone()).isEqualTo(randomTestPhone);
        assertThat(actual.getInstitution()).isEqualTo(randomTestInstitution);
        assertThat(actual.getInstitutionAbbreviation()).isEqualTo(randomTestInstitutionAbbreviation);
        assertThat(actual.getInstitutionWeb()).isEqualTo(randomTestInstitutionWeb);
    }
    @Test
    public void testGetIdDefaultValue() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        Long actual=userDetailsEntity.getId();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetId(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final Long randomTestId = new Random().nextLong();
        userDetailsEntity.setId(randomTestId);
        Long actual=userDetailsEntity.getId();

        assertThat(actual).isEqualTo(randomTestId);
    }

    @Test
    public void testGetFirstNameDefaultValue(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getFirstName();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetFirstName()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setFirstName(randomTestString );
        String actual=userDetailsEntity.getFirstName();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetLastNameDefaultValue()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getLastName();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetLastName()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setLastName(randomTestString);
        String actual=userDetailsEntity.getLastName();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetJobTitleDefaultValue()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getJobTitle();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetJobTitle() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setJobTitle(randomTestString);
        String actual=userDetailsEntity.getJobTitle();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetAddressDefaultValue()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        AddressEntity actual=userDetailsEntity.getAddress();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetAddress() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final AddressEntity addressEntity = new AddressEntity();
        userDetailsEntity.setAddress(addressEntity);
        AddressEntity actual=userDetailsEntity.getAddress();

        assertThat(actual).isEqualTo(addressEntity);
    }

    @Test
    public void testGetEmailDefaultValue()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getEmail();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetEmail()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setEmail(randomTestString);
        String actual=userDetailsEntity.getEmail();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetPhoneDefaultValue() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getPhone();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetPhone() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomNumeric(10);
        userDetailsEntity.setPhone(randomTestString);
        String actual=userDetailsEntity.getPhone();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetInstitution()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getInstitution();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetInstitution()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setInstitution(randomTestString);
        String actual=userDetailsEntity.getInstitution();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetInstitutionAbbreviation()  {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getInstitutionAbbreviation();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetInstitutionAbbreviation() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setInstitutionAbbreviation(randomTestString);
        String actual=userDetailsEntity.getInstitutionAbbreviation();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testGetInstitutionWeb() {
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        String actual=userDetailsEntity.getInstitutionWeb();

        assertThat(actual).isNull();
    }

    @Test
    public void testSetInstitutionWeb() {

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        final String randomTestString = RandomStringUtils.randomAlphanumeric(20);
        userDetailsEntity.setInstitutionWeb(randomTestString);
        String actual=userDetailsEntity.getInstitutionWeb();

        assertThat(actual).isEqualTo(randomTestString);
    }

    @Test
    public void testEqualsInputEqualsInstanceDefaultValue(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final UserDetailsEntity testObject = new UserDetailsEntity();

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void testEqualsInputEqualsInstanceWithRealValues(){
        final UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        final UserDetailsEntity testObject = userDetailsEntity;

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void testEqualsInputIsNull(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        boolean actual=userDetailsEntity.equals(null);

        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void testEqualsDifferentClass(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final UserEntity testObject = new UserEntity();

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(false);
    }

    //getEmail is null and that.getEmail is null, return true
    @Test
    public void testEqualsGetEmailIsNullForObJectAndInstance(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final UserDetailsEntity testObject = TestUtil.getUserDetailsEntity();
        testObject.setEmail(null);

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(true);
    }

    //getEmail is null and that.getEmail is not null, return false
    @Test
    public void testEqualsGetEmailIsNullForInstanceAndNotNullForObJect(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        final UserDetailsEntity testObject = TestUtil.getUserDetailsEntity();

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(false);
    }

    //getEmail is not null and  instance and object getEmail()'s are equal, return true
    @Test
    public void testEqualsInstanceAndObjectGetEmailEqual(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setEmail("testString");
        final UserDetailsEntity testObject = TestUtil.getUserDetailsEntity();
        testObject.setEmail("testString");

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(true);
    }

    //getEmail is not null and  instance and object getEmail()'s are not equal, return false
    @Test
    public void testEqualsInstanceAndObjectGetEmailNotEqual(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setEmail("testString1");
        final UserDetailsEntity testObject = TestUtil.getUserDetailsEntity();
        testObject.setEmail("testString2");

        boolean actual=userDetailsEntity.equals(testObject);

        assertThat(actual).isEqualTo(false);
    }

    //getemail is null , return 0
    @Test
    public void testHashCodeGetEmailIsNull(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();

        int actual=userDetailsEntity.hashCode();

        assertThat(actual).isEqualTo(0);
    }

    //getemail is not null
    @Test
    public void testHashCodeGetEmailIsNotNull(){
        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setEmail("testString");

        int actual=userDetailsEntity.hashCode();
        int expected="testString".hashCode();

        assertThat(actual).isEqualTo(expected);
    }

//    @Test
//    public void testToString() {
//        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
//
//        final Random random = new Random();
//        final Long randomTestId = random.nextLong();
//        final String randomTestFirstName = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTesLastName = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestJobTitle = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestEmail = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestPhone = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestInstitution = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestInstitutionAbbreviation = RandomStringUtils.randomAlphanumeric(20);
//        final String randomTestInstitutionWeb = RandomStringUtils.randomAlphanumeric(20);
//        final ZonedDateTime randomTestCreateDate=ZonedDateTime.now();
//        final ZonedDateTime randomTestLastModifiedDate=ZonedDateTime.now();
//        final Long randomTestVersion = random.nextLong();
//
//        userDetailsEntity.setId(randomTestId);
//        userDetailsEntity.setFirstName(randomTestFirstName );
//        userDetailsEntity.setLastName(randomTesLastName);
//        userDetailsEntity.setJobTitle(randomTestJobTitle);
//        userDetailsEntity.setEmail(randomTestEmail);
//        userDetailsEntity.setPhone(randomTestPhone );
//        userDetailsEntity.setInstitution(randomTestInstitution);
//        userDetailsEntity.setInstitutionAbbreviation(randomTestInstitutionAbbreviation);
//        userDetailsEntity.setInstitutionWeb(randomTestInstitutionWeb);
//        userDetailsEntity.setCreatedDate(randomTestCreateDate);
//        userDetailsEntity.setCreatedDate(randomTestLastModifiedDate);
//        userDetailsEntity.setVersion(randomTestVersion);
//
//        final String actual = userDetailsEntity.toString();
//
//        assertThat(actual).contains(String.valueOf(randomTestId));
//        assertThat(actual).contains(randomTestFirstName);
//        assertThat(actual).contains( randomTesLastName);
//        assertThat(actual).contains(randomTestJobTitle);
//        assertThat(actual).contains(randomTestEmail);
//        assertThat(actual).contains(randomTestPhone);
//        assertThat(actual).contains(randomTestInstitution);
//        assertThat(actual).contains(randomTestInstitutionAbbreviation );
//        assertThat(actual).contains(randomTestInstitutionWeb);
//        assertThat(actual).contains(String.valueOf(randomTestCreateDate));
//        assertThat(actual).contains(String.valueOf(randomTestLastModifiedDate));
//        assertThat(actual).contains(String.valueOf(randomTestVersion));
//    }

}
