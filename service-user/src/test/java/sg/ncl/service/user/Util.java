package sg.ncl.service.user;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;

import java.time.ZonedDateTime;

/**
 * Created by Te Ye on 20-Jun-16.
 */
public class Util {

    public static UserEntity getUserEntity() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());
        userEntity.addRole(User.Role.USER);
        userEntity.setVerificationKey(RandomStringUtils.randomAlphanumeric(20));

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setJobTitle(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setPhone(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setInstitution(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setInstitutionAbbreviation(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setInstitutionWeb(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setCity(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomAlphabetic(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        return userEntity;
    }

    public static UserDetailsEntity getUserDetailsEntity() {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setFirstName(RandomStringUtils.randomAlphanumeric(20));
        entity.setLastName(RandomStringUtils.randomAlphanumeric(20));
        entity.setJobTitle(RandomStringUtils.randomAlphanumeric(20));
        entity.setAddress(Util.getAddressEntity());
        entity.setEmail(RandomStringUtils.randomAlphanumeric(20));
        entity.setPhone(RandomStringUtils.randomAlphanumeric(20));
        entity.setInstitution(RandomStringUtils.randomAlphabetic(20));
        entity.setInstitutionAbbreviation(RandomStringUtils.randomAlphabetic(20));
        entity.setInstitutionWeb(RandomStringUtils.randomAlphabetic(20));
        return entity;
    }

    public static AddressEntity getAddressEntity() {
        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphanumeric(20));
        address.setAddress2(RandomStringUtils.randomAlphanumeric(20));
        address.setCountry(RandomStringUtils.randomAlphanumeric(20));
        address.setRegion(RandomStringUtils.randomAlphanumeric(20));
        address.setCity(RandomStringUtils.randomAlphanumeric(20));
        address.setZipCode(RandomStringUtils.randomAlphanumeric(20));
        return address;
    }
}
