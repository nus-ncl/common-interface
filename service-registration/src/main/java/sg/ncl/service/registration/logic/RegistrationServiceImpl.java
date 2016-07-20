package sg.ncl.service.registration.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.logic.CredentialsService;
import sg.ncl.service.authentication.web.CredentialsInfo;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.*;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMemberStatus;
import sg.ncl.service.team.domain.TeamMemberType;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final CredentialsService credentialsService;
    private final TeamService teamService;
    private final UserService userService;
    private final RegistrationRepository registrationRepository;

    @Autowired
    private final AdapterDeterlab adapterDeterlab;

    @Inject
    protected RegistrationServiceImpl(final CredentialsService credentialsService, final TeamService teamService, final UserService userService, final RegistrationRepository registrationRepository, final DeterlabUserRepository deterlabUserRepository, final ConnectionProperties connectionProperties) {
        this.credentialsService = credentialsService;
        this.teamService = teamService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
        this.adapterDeterlab = new AdapterDeterlab(deterlabUserRepository, connectionProperties);
    }

    public void registerRequestToApplyTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            logger.warn("Team name is empty or null");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            logger.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        try {
            userService.findUser(nclUserId);
        } catch (UserNotFoundException e) {
            logger.warn("No such user, {}", e);
        }

        // will throw exception if name already exists
        checkTeamNameDuplicate(team.getName());

        // no problem with the team
        // create the team
        Team createdTeam = teamService.addTeam(team);

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(nclUserId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(TeamMemberType.OWNER);
        TeamMemberInfo teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        userService.addTeam(nclUserId, createdTeam.getId());
        teamService.addTeamMember(createdTeam.getId(), teamMemberInfo);

        // FIXME call adapter deterlab here
    }

    public void registerRequestToJoinTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            logger.warn("Team name is not found");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            logger.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        Team teamEntity = teamService.getTeamByName(team.getName());
        String teamId = teamEntity.getId();

        JSONObject userObject = new JSONObject();
        userObject.put("uid", adapterDeterlab.getDeterUserIdByNclUserId(nclUserId));
        userObject.put("pid", teamEntity.getName());

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(nclUserId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(TeamMemberType.MEMBER);
        TeamMemberInfo teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        userService.addTeam(nclUserId, teamId);
        teamService.addTeamMember(teamId, teamMemberInfo);

        String resultJSON = adapterDeterlab.joinProject(userObject.toString());
    }

    public void register(Credentials credentials, User user, Team team, boolean isJoinTeam) {

        if (userFormFieldsHasErrors(user)) {
            logger.warn("User form fields has errors {}", user);
            throw new UserFormException();
        }

        if (credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            logger.warn("Credentials password is empty");
            throw new UserFormException();
        }


        if (isJoinTeam == true && (team.getId() == null || team.getId().isEmpty())) {
            logger.warn("Team id from join existing team is empty");
            throw new UserFormException();
        }

        if (isJoinTeam == false && (team.getName() != null || !team.getName().isEmpty())) {
            Team teamEntity = new TeamEntity();
            try {
                logger.info("New team name is {}", team.getName());
                teamEntity = teamService.getTeamByName(team.getName());
            } catch (NullPointerException e) {
                logger.info("This is good, this implies team name is unique");
            }
            if (teamEntity != null && teamEntity.getId() != null) {
                if (!teamEntity.getId().isEmpty()) {
                    logger.warn("Team name duplicate entry found");
                    throw new RegisterTeamNameDuplicateException();
                }
            }
        }

        TeamMemberType memberType;
        String resultJSON;
        String teamId;
        Team teamEntity;
        TeamMemberInfo teamMemberInfo;

        if (isJoinTeam == true) {
            // accept the team data
            teamEntity = teamService.getTeamById(team.getId());
            teamId = team.getId();
        } else {
            // apply for new team
            // check if team already exists
            TeamEntity teamEntity1 = new TeamEntity();
            teamEntity1.setName(team.getName());
            teamEntity1.setVisibility(team.getVisibility());
            teamEntity1.setApplicationDate(ZonedDateTime.now());
            teamEntity1.setDescription(team.getDescription());
            teamEntity1.setWebsite(team.getWebsite());
            teamEntity1.setOrganisationType(team.getOrganisationType());
            teamEntity1.setPrivacy(team.getPrivacy());
            teamEntity = teamService.addTeam(teamEntity1);
            teamId = teamEntity.getId();
        }

        // accept user data from form
        String userId = userService.createUser(user).getId();

        // create the credentials after creating the users
        final CredentialsInfo credentialsInfo = new CredentialsInfo(userId, credentials.getUsername(), credentials.getPassword(), null);
        credentialsService.addCredentials(credentialsInfo);

        if (isJoinTeam == true) {
            // indicate member type based on button click
            memberType = TeamMemberType.MEMBER;
        } else {
            memberType = TeamMemberType.OWNER;
        }

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(userId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(memberType);
        teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        userService.addTeam(userId, teamId);
        teamService.addTeamMember(teamId, teamMemberInfo);

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

        if (isJoinTeam == true) {

            // call python script (create a new user in deterlab)
            // parse in a the json string
            userObject.put("pid", teamEntity.getName());
            resultJSON = adapterDeterlab.joinProjectNewUsers(userObject.toString());

        } else {
            // call python script to apply for new project
            userObject.put("projName", teamEntity.getName());
            userObject.put("projGoals", teamEntity.getDescription());
            userObject.put("pid", teamEntity.getName());
            userObject.put("projWeb", "http://www.nus.edu.sg");
            userObject.put("projOrg", "Academic");
            userObject.put("projPublic", teamEntity.getVisibility());
            resultJSON = adapterDeterlab.applyProjectNewUsers(userObject.toString());
        }

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

    public void approveJoinRequest(String teamId, String userId, User approver) {
        if (teamService.isTeamOwner(approver.getId(), teamId) == false) {
            logger.warn("User {} is not a team owner of Team {}", userId, teamId);
            throw new UserIsNotTeamOwnerException();
        }
        String pid = teamService.getTeamById(teamId).getName();
        // already add to user side when request to join
        JSONObject one = new JSONObject();
        one.put("approverUid", adapterDeterlab.getDeterUserIdByNclUserId(approver.getId()));
        one.put("uid", adapterDeterlab.getDeterUserIdByNclUserId(userId));
        one.put("pid", pid);
        one.put("gid", pid);
        adapterDeterlab.approveJoinRequest(one.toString());
        teamService.changeTeamMemberStatus(userId, teamId, TeamMemberStatus.APPROVED);
    }

    private boolean userFormFieldsHasErrors(User user) {
        boolean errorsFound = false;

        if (user == null) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getFirstName() == null || user.getUserDetails().getFirstName().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getLastName() == null || user.getUserDetails().getLastName().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getJobTitle() == null || user.getUserDetails().getJobTitle().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getEmail() == null || user.getUserDetails().getEmail().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getPhone() == null || user.getUserDetails().getPhone().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getInstitution() == null || user.getUserDetails().getInstitution().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getInstitutionAbbreviation() == null || user.getUserDetails().getInstitutionAbbreviation().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getInstitutionWeb() == null || user.getUserDetails().getInstitutionWeb().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getAddress().getAddress1() == null || user.getUserDetails().getAddress().getAddress1().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getAddress().getCountry() == null || user.getUserDetails().getAddress().getCountry().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getAddress().getRegion() == null || user.getUserDetails().getAddress().getRegion().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getAddress().getCity() == null || user.getUserDetails().getAddress().getCity().isEmpty())) {
            errorsFound = true;
        }

        if (errorsFound == false && (user.getUserDetails().getAddress().getZipCode() == null || user.getUserDetails().getAddress().getZipCode().isEmpty())) {
            errorsFound = true;
        }

        return errorsFound;
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

    private void addUserToRegistrationRepository(String resultJSON, User user, Team team) {

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

    private void checkTeamNameDuplicate(String teamName) {
        Team one = null;
        try {
            logger.info("New team name is {}", teamName);
            one = teamService.getTeamByName(teamName);
        } catch (NullPointerException e) {
            logger.info("This is good, this implies team name is unique");
        }
        if (one != null && one.getId() != null) {
            if (!one.getId().isEmpty()) {
                logger.warn("Team name duplicate entry found");
                throw new RegisterTeamNameDuplicateException();
            }
        }
    }

}
