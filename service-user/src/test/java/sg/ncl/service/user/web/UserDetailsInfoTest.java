package sg.ncl.service.user.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 08-Dec-16.
 */
public class UserDetailsInfoTest {

    private final String firstName = RandomStringUtils.randomAlphanumeric(20);
    private final String lastName = RandomStringUtils.randomAlphanumeric(20);
    private final String jobTitle = RandomStringUtils.randomAlphanumeric(20);
    private final String email = RandomStringUtils.randomAlphanumeric(20);
    private final String phone = RandomStringUtils.randomAlphanumeric(20);
    private final String institution = RandomStringUtils.randomAlphanumeric(20);
    private final String institutionAbbreviation = RandomStringUtils.randomAlphanumeric(20);
    private final String institutionWeb = RandomStringUtils.randomAlphanumeric(20);
    private final UserDetailsInfo userDetailsInfo = new UserDetailsInfo(firstName, lastName, jobTitle, null, email, phone, institution, institutionAbbreviation, institutionWeb);

    @Test
    public void testGetFirstName() throws Exception {
        assertThat(userDetailsInfo.getFirstName()).isEqualTo(firstName);
    }

    @Test
    public void testGetLastName() throws Exception {
        assertThat(userDetailsInfo.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetJobTitle() throws Exception {
        assertThat(userDetailsInfo.getJobTitle()).isEqualTo(jobTitle);
    }

    @Test
    public void testGetAddress() throws Exception {
        assertThat(userDetailsInfo.getAddress()).isNull();
    }

    @Test
    public void testGetEmail() throws Exception {
        assertThat(userDetailsInfo.getEmail()).isEqualTo(email);
    }

    @Test
    public void testGetPhone() throws Exception {
        assertThat(userDetailsInfo.getPhone()).isEqualTo(phone);
    }

    @Test
    public void testGetInstitution() throws Exception {
        assertThat(userDetailsInfo.getInstitution()).isEqualTo(institution);
    }

    @Test
    public void testGetInstitutionAbbreviation() throws Exception {
        assertThat(userDetailsInfo.getInstitutionAbbreviation()).isEqualTo(institutionAbbreviation);
    }

    @Test
    public void testGetInstitutionWeb() throws Exception {
        assertThat(userDetailsInfo.getInstitutionWeb()).isEqualTo(institutionWeb);
    }
}