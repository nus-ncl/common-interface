package sg.ncl.service.user.controllers;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.user.AbstractTest;
import sg.ncl.service.user.data.jpa.entities.AddressEntity;
import sg.ncl.service.user.data.jpa.entities.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Desmond on 24-May-16.
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
    public void getUserTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        Object[] objArray = addUser();
        String idString = (String) objArray[0];
        UserEntity originalEntity = (UserEntity) objArray[1];

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

    private Object[] addUser() throws Exception {
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

        UserEntity newUser = userRepository.save(userEntity);

        Object[] objArray = new Object[2];
        objArray[0] = newUser.getId();
        objArray[1] = userEntity;

        return objArray;
    }
}
