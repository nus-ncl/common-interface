package sg.ncl.service.registration;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.logic.AuthenticationService;
import sg.ncl.service.authentication.logic.CredentialsService;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.repositories.RegistrationRepository;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.services.UserService;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@Service
public class RegistrationService {

    private final AuthenticationService authenticationService;
    private final CredentialsService credentialsService;
    private final TeamService teamService;
    private final UserService userService;
    private final AdapterDeterlab adapterDeterlab;
    private final RegistrationRepository registrationRepository;
//    private final DeterlabUserRepository deterlabUserRepository;

    @Inject
    protected RegistrationService(final AuthenticationService authenticationService, final CredentialsService credentialsService, final TeamService teamService, final UserService userService, final RegistrationRepository registrationRepository) {
        this.authenticationService = authenticationService;
        this.credentialsService = credentialsService;
        this.teamService = teamService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
        this.adapterDeterlab = new AdapterDeterlab();
//        this.deterlabUserRepository = deterlabUserRepository;
//        this.adapterDeterlab = new AdapterDeterlab(deterlabUserRepository);
//        this.adapterDeterlab = new AdapterDeterlab();
    }

    /*
    public void register2(RegistrationData registrationData) {
        // create credentials object
        // create user object
        // create team object

        UserEntity userEntity = new UserEntity();
        UserDetailsEntity userDetails = new UserDetailsEntity();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddress1(registrationData.getUsrAddr());
        addressEntity.setAddress2(registrationData.getUsrAddr2());
        addressEntity.setCountry(registrationData.getUsrCountry());
        addressEntity.setRegion(registrationData.getUsrCity());
        addressEntity.setZipCode(registrationData.getUsrZip());

        // FIXME hardcoded first,last name until can find solution to merge with full name
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");
        userDetails.setEmail(registrationData.getUsrEmail());
        userDetails.setAddress(addressEntity);
        userEntity.setUserDetails(userDetails);

        String userId = userService.addUser(userEntity);

        CredentialsEntity credentialsEntity = new CredentialsEntity();
        credentialsEntity.setId(userId);
        credentialsEntity.setUsername(registrationData.getUsrEmail());
        credentialsEntity.setPassword(registrationData.getClearPassword());

        credentialsService.addCredentials(credentialsEntity);

        TeamEntity teamEntity = new TeamEntity();
    }
    */

    public void register(CredentialsEntity credentials, User user, Team team) {
        // accept user data from form
        String userId = userService.addUser(user);

        // create the credentials after creating the users
        credentials.setId(userId);
        credentialsService.addCredentials(credentials);

        // accept the team data
        TeamEntity teamEntity = teamService.find(team.getId());

        // add user to team and vice versa
        userService.addUserToTeam(userId, team.getId());
        teamService.addUserToTeam(userId, team.getId());

        // call python script (create a new user in deterlab)
        // parse in a the json string
        JSONObject userObject = new JSONObject();
        userObject.put("firstName", user.getUserDetails().getFirstName());
        userObject.put("lastName", user.getUserDetails().getLastName());
        userObject.put("jobTitle", user.getUserDetails().getJobTitle());
        userObject.put("password", credentials.getPassword()); // cannot get from credentialsEntity else will be hashed
        userObject.put("email", user.getUserDetails().getEmail());
        userObject.put("phone", user.getUserDetails().getPhone());
        userObject.put("institution", user.getUserDetails().getInstitution());
        userObject.put("institutionAbbreviation", user.getUserDetails().getInstitutionAbbreviation());
        userObject.put("institutionWeb", user.getUserDetails().getInstitutionWeb());

        userObject.put("address1", user.getUserDetails().getAddress().getAddress1());
        userObject.put("address2", user.getUserDetails().getAddress().getAddress2());
        userObject.put("country", user.getUserDetails().getAddress().getCountry());
        userObject.put("region", user.getUserDetails().getAddress().getRegion());
        userObject.put("city", user.getUserDetails().getAddress().getCity());
        userObject.put("zipCode", user.getUserDetails().getAddress().getZipCode());

        String resultJSON = adapterDeterlab.addUsers(userObject.toString());

        if (getUserCreationStatus(resultJSON).equals("user is created")) {
            // store form fields into registration repository for recreation when required
            addUserToRegistrationRepository(resultJSON, user, teamEntity);

            // call deterlab adapter to store ncluid to deteruid mapping
            addNclUserIdMapping(resultJSON, userId);

        } else {
            // FIXME for debug purposes
            System.out.println(resultJSON);
        }
    }

    private String getUserCreationStatus(String resultJSON) {
        JSONObject result = new JSONObject(resultJSON);
        return result.getString("msg");
    }

    // store ncluid to deteruid mapping
    private void addNclUserIdMapping(String resultJSON, String nclUserId) {
        JSONObject userObject = new JSONObject(resultJSON);
        String deterUserId = userObject.getString("uid");
        adapterDeterlab.saveDeterUserIdMapping(deterUserId, nclUserId);
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

    private void addUserToRegistrationRepository(String resultJSON, User user, TeamEntity team) {

        JSONObject jsonObjectFromAdapter = new JSONObject(resultJSON);
        String uid = jsonObjectFromAdapter.getString("uid");

        // FIXME ncl pid may be different from deter pid
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setPid(team.getId());
        registrationEntity.setUid(uid);

        registrationEntity.setUsrAddr(user.getUserDetails().getAddress().getAddress1());
        registrationEntity.setUsrAddr2(user.getUserDetails().getAddress().getAddress2());
        registrationEntity.setUsrAffil(user.getUserDetails().getInstitution());
        registrationEntity.setUsrAffilAbbrev(user.getUserDetails().getInstitutionAbbreviation());
        registrationEntity.setUsrCity(user.getUserDetails().getAddress().getCity());
        registrationEntity.setUsrCountry(user.getUserDetails().getAddress().getCountry());
        registrationEntity.setUsrState(user.getUserDetails().getAddress().getRegion());

        registrationEntity.setUsrEmail(user.getUserDetails().getEmail());
        registrationEntity.setUsrName(user.getUserDetails().getLastName() + " " + user.getUserDetails().getFirstName());
        registrationEntity.setUsrPhone(user.getUserDetails().getPhone());
        registrationEntity.setUsrTitle(user.getUserDetails().getJobTitle());
        registrationEntity.setUsrZip(user.getUserDetails().getAddress().getZipCode());

        registrationRepository.save(registrationEntity);
    }

}
