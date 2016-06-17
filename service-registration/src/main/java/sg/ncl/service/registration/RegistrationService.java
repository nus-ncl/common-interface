package sg.ncl.service.registration;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.coyote.Adapter;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sg.ncl.service.adapter.AdapterDeterlab;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.logic.AuthenticationService;
import sg.ncl.service.authentication.logic.CredentialsService;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.repositories.RegistrationRepository;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.services.UserService;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
public class RegistrationService {

    private final AuthenticationService authenticationService;
    private final CredentialsService credentialsService;
    private final TeamService teamService;
    private final UserService userService;
    private final AdapterDeterlab adapterDeterlab = new AdapterDeterlab();
    private final RegistrationRepository registrationRepository;

    @Inject
    protected RegistrationService(final AuthenticationService authenticationService, final CredentialsService credentialsService, final TeamService teamService, final UserService userService, final RegistrationRepository registrationRepository) {
        this.authenticationService = authenticationService;
        this.credentialsService = credentialsService;
        this.teamService = teamService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
    }

    public void register(CredentialsEntity credentials, User user, TeamEntity team) {
        // accept user data from form
        String userId = userService.addUser(user);

        // create the credentials after creating the users
        credentials.setId(userId);
        credentialsService.addCredentials(credentials);

        // accept the team data
        TeamEntity teamEntity = teamService.save(team);

        // add user to team and vice versa
        userService.addUserToTeam(userId, teamEntity.getId());
        teamService.addUserToTeam(userId, teamEntity.getId());

        // get all user and team info
        List<String> userFormFieldsList = getUserInfo(userService.find(userId));

        // call python script (create a new user in deterlab)
        // parse in a the json string
        JSONObject userObject = new JSONObject();
        userObject.put("firstname", user.getUserDetails().getFirstName());
        userObject.put("lastname", user.getUserDetails().getLastName());
        userObject.put("password", credentials.getPassword()); // cannot get from credentialsEntity else will be hashed
        userObject.put("email", user.getUserDetails().getEmail());

        String resultJSON = adapterDeterlab.addUsers(userObject.toString());

        // store form fields into registration repository for recreation when required
        addUserToRegistrationRepository(resultJSON, user, team);
    }

//    private void helloWorld() {
//        Process p = null;
//        try {
//            p = Runtime.getRuntime().exec("ssh ncl@172.18.178.10");
//            PrintStream out = new PrintStream(p.getOutputStream());
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            out.println("ls -l");
//            while (in.ready()) {
//                String s = in.readLine();
//                System.out.println(s);
//            }
//            out.println("exit");
//
//            p.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private List<String> getUserInfo(UserEntity userEntity) {
        List<String> ret = new ArrayList<>();
        ret.add(userEntity.getId());
        return ret;
    }

    private void addUserToRegistrationRepository(String resultJSON, User user, TeamEntity team) {

        JSONObject jsonObjectFromAdapter = new JSONObject(resultJSON);
        String uid = jsonObjectFromAdapter.getString("uid");

        // FIXME ncl pid may be different from deter pid
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setPid(team.getId());
        registrationEntity.setUid(uid);

        registrationEntity.setUsrAddr(user.getUserDetails().getAddress().getAddress1());
        registrationEntity.setUsrAddr2(user.getUserDetails().getAddress().getAddress2());
        registrationEntity.setUsrAffil("QWE");
        registrationEntity.setUsrAffilAbbrev("QWE");
        registrationEntity.setUsrCity(user.getUserDetails().getAddress().getRegion());
        registrationEntity.setUsrCountry(user.getUserDetails().getAddress().getCountry());
        registrationEntity.setUsrEmail(user.getUserDetails().getEmail());

        registrationEntity.setUsrName(user.getUserDetails().getLastName() + " " + user.getUserDetails().getFirstName());
        registrationEntity.setUsrPhone(user.getUserDetails().getPhone());
        registrationEntity.setUsrTitle("QWE");
        registrationEntity.setUsrZip(user.getUserDetails().getAddress().getZipCode());

        registrationRepository.save(registrationEntity);
    }

}
