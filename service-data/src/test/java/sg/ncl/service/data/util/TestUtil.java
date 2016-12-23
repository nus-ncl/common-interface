package sg.ncl.service.data.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.data.data.jpa.DataAccessRequestEntity;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataResourceEntity;
import sg.ncl.service.data.domain.DataAccessibility;
import sg.ncl.service.data.domain.DataVisibility;
import sg.ncl.service.transmission.web.ResumableInfo;
import sg.ncl.service.user.data.jpa.AddressEntity;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 9/7/2016.
 */
public class TestUtil {
    public static DataEntity getDataEntity() {
        return getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);
    }

    public static DataEntity getDataEntityWithResources() {
        DataEntity entity = getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);

        DataResourceEntity resourceEntity = getDataResourceEntity();
        List<DataResourceEntity> resources = new ArrayList<>();
        resources.add(resourceEntity);
        entity.setResources(resources);

        return entity;
    }

    public static DataEntity getDataEntityWithApprovedUsers() {
        DataEntity entity = getDataEntity("dataset", "description", "ncl001", DataAccessibility.OPEN, DataVisibility.PUBLIC);

        String user = "bob007";
        entity.addApprovedUser(user);

        return entity;
    }

    public static DataEntity getDataEntity(final String name,
                                           final String description,
                                           final String ownerId,
                                           final DataAccessibility accessibility,
                                           final DataVisibility visibility
                                           )
    {
        final DataEntity entity = new DataEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setContributorId(ownerId);
        entity.setAccessibility(accessibility);
        entity.setVisibility(visibility);
        entity.setReleasedDate(ZonedDateTime.now());

        return entity;
    }

    public static DataResourceEntity getDataResourceEntity() {
        DataResourceEntity entity = new DataResourceEntity();
        entity.setUri("http://" + RandomStringUtils.randomAlphanumeric(20));

        return entity;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResumableInfo getResumableInfo() {
        return new ResumableInfo(1, 1L, RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), null);
    }

    public static DataAccessRequestEntity getDataAccessRequestEntity() {
        DataAccessRequestEntity entity = new DataAccessRequestEntity();
        entity.setId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setDataId(Long.parseLong(RandomStringUtils.randomNumeric(10)));
        entity.setRequesterId(RandomStringUtils.randomAlphanumeric(10));
        entity.setReason(RandomStringUtils.randomAlphanumeric(20));
        entity.setRequestDate(ZonedDateTime.now());
        entity.setApprovedDate(ZonedDateTime.now());
        return entity;
    }

    public static UserEntity getUserEntity() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());
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
}
