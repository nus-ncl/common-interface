package sg.ncl.service.user.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.User;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Desmond
 */
public class UsersControllerTest extends AbstractTest {
    @Inject
    private UserRepository userRepository;

    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllUserWithNoUserInDbTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/users")).andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void getAllUserTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final UserEntity[] userEntityList = new UserEntity[3];

        for (int i = 0; i < 3; i++) {
            Object[] objArray = addUser();
            userEntityList[i] = (UserEntity) objArray[0];
        }

        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String allUserJsonString = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<UserEntity> userEntityList2 = mapper.readValue(allUserJsonString, new TypeReference<List<UserEntity>>(){});

        Assert.assertThat(userEntityList2, IsIterableContainingInAnyOrder.containsInAnyOrder(userEntityList));
    }

    @Test
    public void getUserWithNoUserInDbTest() throws Exception {
        mockMvc.perform(get("/users/" + RandomStringUtils.randomAlphabetic(20)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void getUserTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final UserEntity[] userArray = addUser();
        final String idString = userArray[0].getId();
        final UserEntity originalEntity = userArray[1];

        mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.userDetails.firstName", is(originalEntity.getUserDetails().getFirstName())))
                .andExpect(jsonPath("$.userDetails.lastName", is(originalEntity.getUserDetails().getLastName())))
                .andExpect(jsonPath("$.userDetails.email", is(originalEntity.getUserDetails().getEmail())))
                .andExpect(jsonPath("$.userDetails.phone", is(originalEntity.getUserDetails().getPhone())))
                .andExpect(jsonPath("$.userDetails.address.address1", is(originalEntity.getUserDetails().getAddress().getAddress1())))
                .andExpect(jsonPath("$.userDetails.address.address2", is(originalEntity.getUserDetails().getAddress().getAddress2())))
                .andExpect(jsonPath("$.userDetails.address.country", is(originalEntity.getUserDetails().getAddress().getCountry())))
                .andExpect(jsonPath("$.userDetails.address.region", is(originalEntity.getUserDetails().getAddress().getRegion())))
                .andExpect(jsonPath("$.userDetails.address.zipCode", is(originalEntity.getUserDetails().getAddress().getZipCode())));
    }

    @Test
    public void putUserTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();
        final UserEntity editedEntity = userEntityArray[1];

        mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.userDetails.firstName", is(editedEntity.getUserDetails().getFirstName())))
                .andExpect(jsonPath("$.userDetails.lastName", is(editedEntity.getUserDetails().getLastName())));

        String newFirstName = RandomStringUtils.randomAlphabetic(20);
        editedEntity.getUserDetails().setFirstName(newFirstName);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = mapper.writeValueAsString(editedEntity);

        mockMvc.perform(put("/users/" + idString).contentType(contentType).content(jsonInString))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.userDetails.firstName", is(newFirstName)))
                .andExpect(jsonPath("$.userDetails.lastName", is(editedEntity.getUserDetails().getLastName())));
    }

    @Transactional
    private UserEntity[] addUser() throws Exception {
        final UserEntity userEntity = new UserEntity();
        userEntity.setApplicationDate(ZonedDateTime.now());

        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setLastName(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setEmail(RandomStringUtils.randomAlphabetic(20));
        userDetailsEntity.setPhone(RandomStringUtils.randomAlphabetic(20));

        final AddressEntity address = new AddressEntity();
        address.setAddress1(RandomStringUtils.randomAlphabetic(20));
        address.setAddress2(RandomStringUtils.randomAlphabetic(20));
        address.setCountry(RandomStringUtils.randomAlphabetic(20));
        address.setRegion(RandomStringUtils.randomAlphabetic(20));
        address.setZipCode(RandomStringUtils.randomAlphabetic(20));

        userDetailsEntity.setAddress(address);
        userEntity.setUserDetails(userDetailsEntity);

        UserEntity saveUser = userRepository.save(userEntity);

        final UserEntity[] userArray = new UserEntity[3];
        userArray[0] = saveUser;
        userArray[1] = userEntity;

        return userArray;
    }
}
