package sg.ncl.service.user.web;

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
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.Util;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.data.jpa.UserRepository;

import javax.inject.Inject;
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
    public void getAllUsersTest() throws Exception {
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
                .andExpect(jsonPath("$.userDetails.jobTitle", is(originalEntity.getUserDetails().getJobTitle())))
                .andExpect(jsonPath("$.userDetails.email", is(originalEntity.getUserDetails().getEmail())))
                .andExpect(jsonPath("$.userDetails.phone", is(originalEntity.getUserDetails().getPhone())))
                .andExpect(jsonPath("$.userDetails.institution", is(originalEntity.getUserDetails().getInstitution())))
                .andExpect(jsonPath("$.userDetails.institutionAbbreviation", is(originalEntity.getUserDetails().getInstitutionAbbreviation())))
                .andExpect(jsonPath("$.userDetails.institutionWeb", is(originalEntity.getUserDetails().getInstitutionWeb())))
                .andExpect(jsonPath("$.userDetails.address.address1", is(originalEntity.getUserDetails().getAddress().getAddress1())))
                .andExpect(jsonPath("$.userDetails.address.address2", is(originalEntity.getUserDetails().getAddress().getAddress2())))
                .andExpect(jsonPath("$.userDetails.address.country", is(originalEntity.getUserDetails().getAddress().getCountry())))
                .andExpect(jsonPath("$.userDetails.address.region", is(originalEntity.getUserDetails().getAddress().getRegion())))
                .andExpect(jsonPath("$.userDetails.address.city", is(originalEntity.getUserDetails().getAddress().getCity())))
                .andExpect(jsonPath("$.userDetails.address.zipCode", is(originalEntity.getUserDetails().getAddress().getZipCode())));
    }

    @Test
    public void putUserTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        UserEntity[] userArray = addUser();
        UserEntity userEntity = userArray[0];

        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();

         // get user
        MvcResult result = mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        UserEntity userEntityFromDb = mapper.readValue(jsonString, UserEntity.class);
        String originalLastName = userEntityFromDb.getUserDetails().getLastName();

        // change first name
        String newFirstName = RandomStringUtils.randomAlphabetic(20);
        userEntityFromDb.getUserDetails().setFirstName(newFirstName);

        String jsonInString = mapper.writeValueAsString(userEntityFromDb.getUserDetails());

        // put
        mockMvc.perform(put("/users/" + idString).contentType(contentType).content(jsonInString))
                .andExpect(status().isAccepted());

        // check if first name is new first name and last name is the same
        mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.userDetails.firstName", is(newFirstName)))
                .andExpect(jsonPath("$.userDetails.lastName", is(originalLastName)));
    }

    @Test
    public void putUserNullFieldTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final UserEntity[] userEntityArray = addUser();
        final String idString = userEntityArray[0].getId();

        // get user
        MvcResult result = mockMvc.perform(get("/users/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String jsonString = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        UserEntity userEntity = mapper.readValue(jsonString, UserEntity.class);
        userEntity.getUserDetails().setFirstName(null);

        String jsonInString = mapper.writeValueAsString(userEntity);

        // put
        mockMvc.perform(put("/users/" + idString).contentType(contentType).content(jsonInString))
                .andExpect(status().isAccepted());
    }

    @Test
    public void putUserWithWrongIdTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final String idString = "123456";

        // put
        mockMvc.perform(put("/users/" + idString).contentType(contentType).content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    private UserEntity[] addUser() throws Exception {
        UserEntity userEntity = Util.getUserEntity();
        UserEntity saveUser = userRepository.save(userEntity);

        final UserEntity[] userArray = new UserEntity[2];
        userArray[0] = saveUser;
        userArray[1] = userEntity;

        return userArray;
    }
}
