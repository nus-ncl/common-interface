package sg.ncl.service.registration;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
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

    public static UserEntity getUserEntity() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        userDetailsEntity.setPhone(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomAlphabetic(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        return userEntity;
    }

    public static CredentialsEntity getCredentialsEntity() {
        final CredentialsEntity credentialsEntity = new CredentialsEntity();
//        credentialsEntity.setId(RandomStringUtils.randomAlphabetic(20));
        credentialsEntity.setUsername(RandomStringUtils.randomAlphabetic(8) + "@nus.edu.sg");
        credentialsEntity.setPassword("deterinavm");
        return credentialsEntity;
    }
}
