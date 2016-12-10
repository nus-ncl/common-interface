package sg.ncl.service.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.user.util.TestUtil;
import sg.ncl.service.user.data.jpa.UserDetailsEntity;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Desmond
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsersController.class, secure = true)
@ContextConfiguration(classes = {UsersController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class UsersControllerTest {

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(userService).isMock()).isTrue();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
    }

    @Test
    public void getAllUserWithNoUserInDbTest() throws Exception {
        mockMvc.perform(get(UsersController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        final List<User> list = new ArrayList<>();
        final UserEntity entity1 = TestUtil.getUserEntity();
        final UserEntity entity2 = TestUtil.getUserEntity();
        list.add(entity1);
        list.add(entity2);

        when(userService.getAll()).thenReturn(list);

        mockMvc.perform(get(UsersController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", Matchers.is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].status", Matchers.is(equalTo(entity1.getStatus().toString()))))
                .andExpect(jsonPath("$[0].verificationKey", Matchers.is(equalTo(entity1.getVerificationKey()))))
                .andExpect(jsonPath("$[0].loginActivities", Matchers.is(equalTo(entity1.getLoginActivities()))))
                .andExpect(jsonPath("$[0].teams", Matchers.is(equalTo(entity1.getTeams()))))

                .andExpect(jsonPath("$[0].userDetails.id", Matchers.is(equalTo(entity1.getUserDetails().getId()))))
                .andExpect(jsonPath("$[0].userDetails.email", Matchers.is(equalTo(entity1.getUserDetails().getEmail()))))
                .andExpect(jsonPath("$[0].userDetails.firstName", Matchers.is(equalTo(entity1.getUserDetails().getFirstName()))))
                .andExpect(jsonPath("$[0].userDetails.lastName", Matchers.is(equalTo(entity1.getUserDetails().getLastName()))))
                .andExpect(jsonPath("$[0].userDetails.institution", Matchers.is(equalTo(entity1.getUserDetails().getInstitution()))))
                .andExpect(jsonPath("$[0].userDetails.institutionAbbreviation", Matchers.is(equalTo(entity1.getUserDetails().getInstitutionAbbreviation()))))
                .andExpect(jsonPath("$[0].userDetails.institutionWeb", Matchers.is(equalTo(entity1.getUserDetails().getInstitutionWeb()))))
                .andExpect(jsonPath("$[0].userDetails.jobTitle", Matchers.is(equalTo(entity1.getUserDetails().getJobTitle()))))
                .andExpect(jsonPath("$[0].userDetails.phone", Matchers.is(equalTo(entity1.getUserDetails().getPhone()))))

                .andExpect(jsonPath("$[0].userDetails.address.id", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getId()))))
                .andExpect(jsonPath("$[0].userDetails.address.address1", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getAddress1()))))
                .andExpect(jsonPath("$[0].userDetails.address.address2", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getAddress2()))))
                .andExpect(jsonPath("$[0].userDetails.address.city", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getCity()))))
                .andExpect(jsonPath("$[0].userDetails.address.country", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getCountry()))))
                .andExpect(jsonPath("$[0].userDetails.address.region", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getRegion()))))
                .andExpect(jsonPath("$[0].userDetails.address.zipCode", Matchers.is(equalTo(entity1.getUserDetails().getAddress().getZipCode()))))

                .andExpect(jsonPath("$[1].id", Matchers.is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].status", Matchers.is(equalTo(entity2.getStatus().toString()))))
                .andExpect(jsonPath("$[1].verificationKey", Matchers.is(equalTo(entity2.getVerificationKey()))))
                .andExpect(jsonPath("$[1].loginActivities", Matchers.is(equalTo(entity2.getLoginActivities()))))
                .andExpect(jsonPath("$[1].teams", Matchers.is(equalTo(entity2.getTeams()))))

                .andExpect(jsonPath("$[1].userDetails.id", Matchers.is(equalTo(entity2.getUserDetails().getId()))))
                .andExpect(jsonPath("$[1].userDetails.email", Matchers.is(equalTo(entity2.getUserDetails().getEmail()))))
                .andExpect(jsonPath("$[1].userDetails.firstName", Matchers.is(equalTo(entity2.getUserDetails().getFirstName()))))
                .andExpect(jsonPath("$[1].userDetails.lastName", Matchers.is(equalTo(entity2.getUserDetails().getLastName()))))
                .andExpect(jsonPath("$[1].userDetails.institution", Matchers.is(equalTo(entity2.getUserDetails().getInstitution()))))
                .andExpect(jsonPath("$[1].userDetails.institutionAbbreviation", Matchers.is(equalTo(entity2.getUserDetails().getInstitutionAbbreviation()))))
                .andExpect(jsonPath("$[1].userDetails.institutionWeb", Matchers.is(equalTo(entity2.getUserDetails().getInstitutionWeb()))))
                .andExpect(jsonPath("$[1].userDetails.jobTitle", Matchers.is(equalTo(entity2.getUserDetails().getJobTitle()))))
                .andExpect(jsonPath("$[1].userDetails.phone", Matchers.is(equalTo(entity2.getUserDetails().getPhone()))))

                .andExpect(jsonPath("$[1].userDetails.address.id", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getId()))))
                .andExpect(jsonPath("$[1].userDetails.address.address1", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getAddress1()))))
                .andExpect(jsonPath("$[1].userDetails.address.address2", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getAddress2()))))
                .andExpect(jsonPath("$[1].userDetails.address.city", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getCity()))))
                .andExpect(jsonPath("$[1].userDetails.address.country", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getCountry()))))
                .andExpect(jsonPath("$[1].userDetails.address.region", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getRegion()))))
                .andExpect(jsonPath("$[1].userDetails.address.zipCode", Matchers.is(equalTo(entity2.getUserDetails().getAddress().getZipCode()))));

    }

    @Test
    public void getUserWithNoUserInDbTest() throws Exception {
        mockMvc.perform(get(UsersController.PATH + "/" + RandomStringUtils.randomAlphabetic(20)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));;
    }

    @Test
    public void getUserTest() throws Exception {
        final User user = TestUtil.getUserEntity();
        final String id = RandomStringUtils.randomAlphabetic(20);

        when(userService.getUser(anyString())).thenReturn(user);

        mockMvc.perform(get(UsersController.PATH + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", Matchers.is(equalTo(user.getId()))))
                .andExpect(jsonPath("$.status", Matchers.is(equalTo(user.getStatus().toString()))))
                .andExpect(jsonPath("$.verificationKey", Matchers.is(equalTo(user.getVerificationKey()))))
                .andExpect(jsonPath("$.loginActivities", Matchers.is(equalTo(user.getLoginActivities()))))
                .andExpect(jsonPath("$.teams", Matchers.is(equalTo(user.getTeams()))))

                .andExpect(jsonPath("$.userDetails.email", Matchers.is(equalTo(user.getUserDetails().getEmail()))))
                .andExpect(jsonPath("$.userDetails.firstName", Matchers.is(equalTo(user.getUserDetails().getFirstName()))))
                .andExpect(jsonPath("$.userDetails.lastName", Matchers.is(equalTo(user.getUserDetails().getLastName()))))
                .andExpect(jsonPath("$.userDetails.institution", Matchers.is(equalTo(user.getUserDetails().getInstitution()))))
                .andExpect(jsonPath("$.userDetails.institutionAbbreviation", Matchers.is(equalTo(user.getUserDetails().getInstitutionAbbreviation()))))
                .andExpect(jsonPath("$.userDetails.institutionWeb", Matchers.is(equalTo(user.getUserDetails().getInstitutionWeb()))))
                .andExpect(jsonPath("$.userDetails.jobTitle", Matchers.is(equalTo(user.getUserDetails().getJobTitle()))))
                .andExpect(jsonPath("$.userDetails.phone", Matchers.is(equalTo(user.getUserDetails().getPhone()))))

                .andExpect(jsonPath("$.userDetails.address.address1", Matchers.is(equalTo(user.getUserDetails().getAddress().getAddress1()))))
                .andExpect(jsonPath("$.userDetails.address.address2", Matchers.is(equalTo(user.getUserDetails().getAddress().getAddress2()))))
                .andExpect(jsonPath("$.userDetails.address.city", Matchers.is(equalTo(user.getUserDetails().getAddress().getCity()))))
                .andExpect(jsonPath("$.userDetails.address.country", Matchers.is(equalTo(user.getUserDetails().getAddress().getCountry()))))
                .andExpect(jsonPath("$.userDetails.address.region", Matchers.is(equalTo(user.getUserDetails().getAddress().getRegion()))))
                .andExpect(jsonPath("$.userDetails.address.zipCode", Matchers.is(equalTo(user.getUserDetails().getAddress().getZipCode()))));
    }

    @Test
    public void putUserTest() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(userService.updateUser(anyString(), any(UserInfo.class))).thenReturn(entity);

        mockMvc.perform(put(UsersController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", Matchers.is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$.status", Matchers.is(equalTo(entity.getStatus().toString()))))
                .andExpect(jsonPath("$.verificationKey", Matchers.is(equalTo(entity.getVerificationKey()))))
                .andExpect(jsonPath("$.loginActivities", Matchers.is(equalTo(entity.getLoginActivities()))))
                .andExpect(jsonPath("$.teams", Matchers.is(equalTo(entity.getTeams()))))

                .andExpect(jsonPath("$.userDetails.email", Matchers.is(equalTo(entity.getUserDetails().getEmail()))))
                .andExpect(jsonPath("$.userDetails.firstName", Matchers.is(equalTo(entity.getUserDetails().getFirstName()))))
                .andExpect(jsonPath("$.userDetails.lastName", Matchers.is(equalTo(entity.getUserDetails().getLastName()))))
                .andExpect(jsonPath("$.userDetails.institution", Matchers.is(equalTo(entity.getUserDetails().getInstitution()))))
                .andExpect(jsonPath("$.userDetails.institutionAbbreviation", Matchers.is(equalTo(entity.getUserDetails().getInstitutionAbbreviation()))))
                .andExpect(jsonPath("$.userDetails.institutionWeb", Matchers.is(equalTo(entity.getUserDetails().getInstitutionWeb()))))
                .andExpect(jsonPath("$.userDetails.jobTitle", Matchers.is(equalTo(entity.getUserDetails().getJobTitle()))))
                .andExpect(jsonPath("$.userDetails.phone", Matchers.is(equalTo(entity.getUserDetails().getPhone()))))

                .andExpect(jsonPath("$.userDetails.address.address1", Matchers.is(equalTo(entity.getUserDetails().getAddress().getAddress1()))))
                .andExpect(jsonPath("$.userDetails.address.address2", Matchers.is(equalTo(entity.getUserDetails().getAddress().getAddress2()))))
                .andExpect(jsonPath("$.userDetails.address.city", Matchers.is(equalTo(entity.getUserDetails().getAddress().getCity()))))
                .andExpect(jsonPath("$.userDetails.address.country", Matchers.is(equalTo(entity.getUserDetails().getAddress().getCountry()))))
                .andExpect(jsonPath("$.userDetails.address.region", Matchers.is(equalTo(entity.getUserDetails().getAddress().getRegion()))))
                .andExpect(jsonPath("$.userDetails.address.zipCode", Matchers.is(equalTo(entity.getUserDetails().getAddress().getZipCode()))));
    }

    @Test
    public void putUserNullFirstNameTest() throws Exception {
        final UserEntity entity = new UserEntity();
        final UserDetailsEntity userDetailsEntity = TestUtil.getUserDetailsEntity();
        userDetailsEntity.setFirstName(null);
        entity.setUserDetails(userDetailsEntity);

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(userService.updateUser(anyString(), any(UserInfo.class))).thenReturn(entity);

        mockMvc.perform(put(UsersController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())

                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", Matchers.is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$.status", Matchers.is(equalTo(entity.getStatus().toString()))))
                .andExpect(jsonPath("$.verificationKey", Matchers.is(equalTo(entity.getVerificationKey()))))
                .andExpect(jsonPath("$.loginActivities", Matchers.is(equalTo(entity.getLoginActivities()))))
                .andExpect(jsonPath("$.teams", Matchers.is(equalTo(entity.getTeams()))))

                .andExpect(jsonPath("$.userDetails.email", Matchers.is(equalTo(entity.getUserDetails().getEmail()))))
                .andExpect(jsonPath("$.userDetails.firstName", Matchers.is(equalTo(entity.getUserDetails().getFirstName()))))
                .andExpect(jsonPath("$.userDetails.lastName", Matchers.is(equalTo(entity.getUserDetails().getLastName()))))
                .andExpect(jsonPath("$.userDetails.institution", Matchers.is(equalTo(entity.getUserDetails().getInstitution()))))
                .andExpect(jsonPath("$.userDetails.institutionAbbreviation", Matchers.is(equalTo(entity.getUserDetails().getInstitutionAbbreviation()))))
                .andExpect(jsonPath("$.userDetails.institutionWeb", Matchers.is(equalTo(entity.getUserDetails().getInstitutionWeb()))))
                .andExpect(jsonPath("$.userDetails.jobTitle", Matchers.is(equalTo(entity.getUserDetails().getJobTitle()))))
                .andExpect(jsonPath("$.userDetails.phone", Matchers.is(equalTo(entity.getUserDetails().getPhone()))))

                .andExpect(jsonPath("$.userDetails.address.address1", Matchers.is(equalTo(entity.getUserDetails().getAddress().getAddress1()))))
                .andExpect(jsonPath("$.userDetails.address.address2", Matchers.is(equalTo(entity.getUserDetails().getAddress().getAddress2()))))
                .andExpect(jsonPath("$.userDetails.address.city", Matchers.is(equalTo(entity.getUserDetails().getAddress().getCity()))))
                .andExpect(jsonPath("$.userDetails.address.country", Matchers.is(equalTo(entity.getUserDetails().getAddress().getCountry()))))
                .andExpect(jsonPath("$.userDetails.address.region", Matchers.is(equalTo(entity.getUserDetails().getAddress().getRegion()))))
                .andExpect(jsonPath("$.userDetails.address.zipCode", Matchers.is(equalTo(entity.getUserDetails().getAddress().getZipCode()))));
    }

    @Test
    public void putUserWithWrongIdTest() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(userService.updateUser(anyString(), any(UserInfo.class))).thenThrow(new UserNotFoundException(entity.getId()));

        mockMvc.perform(put(UsersController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    public void verifyEmail() throws Exception {
        final VerificationKeyInfo verificationKeyInfo = new VerificationKeyInfo("key");

        final byte[] content = mapper.writeValueAsBytes(verificationKeyInfo);

        when(userService.verifyEmail(anyString(), anyString(), anyString())).thenReturn(UserStatus.PENDING);

        mockMvc.perform(put(UsersController.PATH + "/id/emails/emailBase64").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserStatusGood() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setStatus(UserStatus.CLOSED);
        final List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.getAuthority());

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(userService.updateUserStatus(anyString(), any(UserStatus.class))).thenReturn(entity);

        mockMvc.perform(put(UsersController.PATH + "/" + entity.getId() + "/status/" + UserStatus.CLOSED).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())

                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", Matchers.is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$.status", Matchers.is(equalTo(UserStatus.CLOSED.toString()))));
    }

    @Test
    public void updateUserStatusUnauthorizedException() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(authentication.getPrincipal()).thenReturn(null);

        mockMvc.perform(put(UsersController.PATH + "/id/status/" + UserStatus.CLOSED).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUserStatusBadClaimsType() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(claims.get(JwtToken.KEY)).thenReturn(null);

        mockMvc.perform(put(UsersController.PATH + "/id/status/" + UserStatus.CLOSED).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserStatusNotAdmin() throws Exception {
        final UserEntity entity = TestUtil.getUserEntity();
        entity.setStatus(UserStatus.CLOSED);
        final List<String> roles = new ArrayList<>();
        roles.add(Role.USER.getAuthority());

        mapper.registerModule(new JavaTimeModule());
        final byte[] content = mapper.writeValueAsBytes(new UserInfo(entity));

        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        mockMvc.perform(put(UsersController.PATH + "/" + entity.getId() + "/status/" + UserStatus.CLOSED).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }
}
