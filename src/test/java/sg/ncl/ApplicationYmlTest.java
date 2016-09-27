package sg.ncl;

import org.junit.Test;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Te Ye
 */
public class ApplicationYmlTest {

    private String authentications = "/authentications";
    private String registrations = "/registrations";
    private String usersId = "/users/(\\w|-)+";
    private String usersEmail = "/users/(\\w|-)+/emails/\\w+=*";
    private String teamVisibility = "/teams\\?visibility=PUBLIC";
    private String teamName = "/teams\\?name=(\\w| )+";

    @Test
    public void testAuthentications() throws Exception {
        assertThat(Pattern.matches(authentications, "/authentications")).isTrue();
        assertThat(Pattern.matches(authentications, "/authentications/")).isFalse();
        assertThat(Pattern.matches(authentications, "authentications")).isFalse();
        assertThat(Pattern.matches(authentications, "/authentication")).isFalse();
    }

    @Test
    public void testRegistrations() throws Exception {
        assertThat(Pattern.matches(registrations, "/registrations")).isTrue();
        assertThat(Pattern.matches(registrations, "/registrations/")).isFalse();
        assertThat(Pattern.matches(registrations, "registrations")).isFalse();
        assertThat(Pattern.matches(registrations, "/registration")).isFalse();
    }

    @Test
    public void testUsersId() throws Exception {
        assertThat(Pattern.matches(usersId, "/users/1")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/-A")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/-")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/A")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/A-")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/A-1")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/AA-")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/AAA-123")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/ ")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/ A")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/ -")).isFalse();
        assertThat(Pattern.matches(usersId, "/users//")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/")).isFalse();
        assertThat(Pattern.matches(usersId, "/users")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/A ")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/AAA-123/")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/AAA-123/emails")).isFalse();
    }

    @Test
    public void testUsersEmail() throws Exception {
        assertThat(Pattern.matches(usersEmail, "/users/A/emails/K")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/1/emails/1")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails/1")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails/A=")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123/emails/AAAc")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123/emails/AAAc=")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123/emails/AAAc==")).isTrue();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123/emails/AAAc===")).isTrue();
        assertThat(Pattern.matches(usersId, "/users/ ")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/ A")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/ -")).isFalse();
        assertThat(Pattern.matches(usersId, "/users/")).isFalse();
        assertThat(Pattern.matches(usersId, "/users")).isFalse();
        assertThat(Pattern.matches(usersId, "/users//emails//A=")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123/emails/AAAc=/")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/AAA-123")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails/=")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/ /emails/ ")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/ /emails/=")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails/")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails//")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/-/emails/-=")).isFalse();
        assertThat(Pattern.matches(usersEmail, "/users/*/emails/*=")).isFalse();
    }

    @Test
    public void testTeamVisibility() throws Exception {
        assertThat(Pattern.matches(teamVisibility, "/teams?visibility=PUBLIC")).isTrue();
        assertThat(Pattern.matches(teamVisibility, "/teams")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams/")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "//teams?name=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "//teams?visibility=")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams?visibility=PUBLIC ")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams?visbility=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams\\/?visibility=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams/visibility=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams? visibility=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams?visibility= PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams?visibility=PUBLIC/")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "/teams/?visibility=PUBLIC/")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "//teams?visibility=PUBLIc")).isFalse();
        assertThat(Pattern.matches(teamVisibility, "//teams?visibility=PRIVATE")).isFalse();
    }

    @Test
    public void testTeamName() throws Exception {
        assertThat(Pattern.matches(teamName, "/teams?name=AAA")).isTrue();
        assertThat(Pattern.matches(teamName, "/teams?name= ")).isTrue();
        assertThat(Pattern.matches(teamName, "/teams?name=   ")).isTrue();
        assertThat(Pattern.matches(teamName, "/teams?name= A")).isTrue();
        assertThat(Pattern.matches(teamName, "/teams?name=AAA AAA")).isTrue();
        assertThat(Pattern.matches(teamName, "/teams")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams/")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?visibility=PUBLIC")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name=AAA AAA*")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name=AAA-AAA")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name=AAA/")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams/?name=AAA")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name= /")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name=   /")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name=")).isFalse();
        assertThat(Pattern.matches(teamName, "/teams?name =A")).isFalse();
    }
}
