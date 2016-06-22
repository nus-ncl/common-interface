package sg.ncl.service.registration;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;

import java.time.ZonedDateTime;

/**
 * Created by dcsyeoty on 15-Jun-16.
 */
public class Util {

    public static TeamEntity getTeamEntity() {
        final TeamEntity entity = new TeamEntity();
        entity.setId(RandomStringUtils.randomAlphanumeric(20));
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setApplicationDate(ZonedDateTime.now());
        return entity;
    }

    public static TeamEntity getInvalidTeamEntity() {
        final TeamEntity entity = new TeamEntity();
        entity.setId(null);
        entity.setName(RandomStringUtils.randomAlphanumeric(20));
        entity.setDescription(RandomStringUtils.randomAlphanumeric(20));
        entity.setApplicationDate(ZonedDateTime.now());
        return entity;
    }

    public static UserEntity getUserEntity() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setJobTitle(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        userDetailsEntity.setPhone(RandomStringUtils.randomNumeric(20));
        userDetailsEntity.setInstitution(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setInstitutionAbbreviation(RandomStringUtils.randomAlphabetic(15));
        userDetailsEntity.setInstitutionWeb(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setCity(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomNumeric(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        return userEntity;
    }

    public static UserEntity getInvalidUserEntity() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        // purposely set null
        userDetailsEntity.setFirstName(null);
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setJobTitle(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        userDetailsEntity.setPhone(RandomStringUtils.randomNumeric(20));
        userDetailsEntity.setInstitution(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setInstitutionAbbreviation(RandomStringUtils.randomAlphabetic(15));
        userDetailsEntity.setInstitutionWeb(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setCity(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomNumeric(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        return userEntity;
    }

    public static CredentialsEntity getCredentialsEntity() {
        final CredentialsEntity credentialsEntity = new CredentialsEntity();
        credentialsEntity.setUsername(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        credentialsEntity.setPassword("deterinavm");
        return credentialsEntity;
    }

    public static CredentialsEntity getInvalidCredentialsEntity() {
        final CredentialsEntity credentialsEntity = new CredentialsEntity();
        credentialsEntity.setUsername(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        credentialsEntity.setPassword(null);
        return credentialsEntity;
    }

    public static RegistrationEntity getRegistrationEntity() {
        final RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setPid(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUid(RandomStringUtils.randomAlphabetic(20));

        registrationEntity.setUsrAddr(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrAddr2(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrAffil(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrAffilAbbrev(RandomStringUtils.randomAlphabetic(15));
        registrationEntity.setUsrCity(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrCountry(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrState(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrEmail(RandomStringUtils.randomAlphabetic(20));

        registrationEntity.setUsrName(RandomStringUtils.randomAlphabetic(4) + " " + RandomStringUtils.randomAlphabetic(4));
        registrationEntity.setUsrPhone(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrTitle(RandomStringUtils.randomAlphabetic(20));
        registrationEntity.setUsrZip(RandomStringUtils.randomAlphabetic(20));

        return registrationEntity;
    }
}
