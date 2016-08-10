package sg.ncl.service.registration.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.web.CredentialsInfo;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.*;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final CredentialsService credentialsService;
    private final TeamService teamService;
    private final UserService userService;
    private final RegistrationRepository registrationRepository;

    // FIXME: what is this autowired?
    @Autowired
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    RegistrationServiceImpl(@NotNull final CredentialsService credentialsService, @NotNull final TeamService teamService, @NotNull final UserService userService, @NotNull final RegistrationRepository registrationRepository, final DeterLabUserRepository deterLabUserRepository, final ConnectionProperties connectionProperties) {
        this.credentialsService = credentialsService;
        this.teamService = teamService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
        // FIXME: why is this getting replaced?
        this.adapterDeterLab = new AdapterDeterLab(deterLabUserRepository, connectionProperties);
    }

    @Transactional
    public void registerRequestToApplyTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Team name is empty or null");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            log.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        try {
            userService.findUser(nclUserId);
        } catch (UserNotFoundException e) {
            log.warn("No such user, {}", e);
        }

        // will throw exception if name already exists
        checkTeamNameDuplicate(team.getName());

        // no problem with the team
        // create the team
        Team createdTeam = teamService.createTeam(team);

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(nclUserId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(MemberType.OWNER);
        TeamMemberInfo teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        // FIXME call adapter deterlab here
        JSONObject mainObject = new JSONObject();
        mainObject.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(nclUserId));
        mainObject.put("projName", team.getName());
        mainObject.put("pid", team.getName());
        mainObject.put("projGoals", team.getDescription());
        mainObject.put("projWeb", team.getWebsite());
        mainObject.put("projOrg", team.getOrganisationType());
        mainObject.put("projPublic", team.getVisibility());
        String resultJSON = adapterDeterLab.applyProject(mainObject.toString());

        userService.addTeam(nclUserId, createdTeam.getId());
        teamService.addMember(createdTeam.getId(), teamMemberInfo);
    }

    @Transactional
    public void registerRequestToJoinTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Team name is not found");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            log.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        Team teamEntity = teamService.getTeamByName(team.getName());
        String teamId = teamEntity.getId();

        JSONObject userObject = new JSONObject();
        userObject.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(nclUserId));
        userObject.put("pid", teamEntity.getName());

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(nclUserId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(MemberType.MEMBER);
        TeamMemberInfo teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        userService.addTeam(nclUserId, teamId);
        teamService.addMember(teamId, teamMemberInfo);

        String resultJSON = adapterDeterLab.joinProject(userObject.toString());
    }

    @Transactional
    public void register(Credentials credentials, User user, Team team, boolean isJoinTeam) {

        if (userFormFieldsHasErrors(user)) {
            log.warn("User form fields has errors {}", user);
            throw new UserFormException();
        }

        if (credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            log.warn("Credentials password is empty");
            throw new UserFormException();
        }

        if (isJoinTeam == true && (team.getId() == null || team.getId().isEmpty())) {
            log.warn("Team id from join existing team is empty");
            throw new UserFormException();
        }

        if (isJoinTeam == false && (team.getName() != null || !team.getName().isEmpty())) {
            Team teamEntity = new TeamEntity();
            try {
                log.info("New team name is {}", team.getName());
                teamEntity = teamService.getTeamByName(team.getName());
            } catch (NullPointerException e) {
                log.info("This is good, this implies team name is unique");
            }
            if (teamEntity != null && teamEntity.getId() != null) {
                if (!teamEntity.getId().isEmpty()) {
                    log.warn("Team name duplicate entry found");
                    throw new RegisterTeamNameDuplicateException();
                }
            }
        }

        MemberType memberType;
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
            teamEntity = teamService.createTeam(teamEntity1);
            teamId = teamEntity.getId();
        }

        // accept user data from form
        String userId = userService.createUser(user).getId();

        // create the credentials after creating the users
        final CredentialsInfo credentialsInfo = new CredentialsInfo(userId, credentials.getUsername(), credentials.getPassword(), null);
        credentialsService.addCredentials(credentialsInfo);

        if (isJoinTeam == true) {
            // indicate member type based on button click
            memberType = MemberType.MEMBER;
        } else {
            memberType = MemberType.OWNER;
        }

        TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setUserId(userId);
        teamMemberEntity.setJoinedDate(ZonedDateTime.now());
        teamMemberEntity.setMemberType(memberType);
        teamMemberInfo = new TeamMemberInfo(teamMemberEntity);

        userService.addTeam(userId, teamId);
        teamService.addMember(teamId, teamMemberInfo);

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
            resultJSON = adapterDeterLab.joinProjectNewUsers(userObject.toString());

        } else {
            // call python script to apply for new project
            userObject.put("projName", teamEntity.getName());
            userObject.put("projGoals", teamEntity.getDescription());
            userObject.put("pid", teamEntity.getName());
            userObject.put("projWeb", "http://www.nus.edu.sg");
            userObject.put("projOrg", "Academic");
            userObject.put("projPublic", teamEntity.getVisibility());
            resultJSON = adapterDeterLab.applyProjectNewUsers(userObject.toString());
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

    @Transactional
    public void approveJoinRequest(String teamId, String userId, User approver) {
        if (!teamService.isOwner(teamId, approver.getId())) {
            log.warn("User {} is not a team owner of Team {}", userId, teamId);
            throw new UserIsNotTeamOwnerException();
        }
        String pid = teamService.getTeamById(teamId).getName();
        // already add to user side when request to join
        JSONObject one = new JSONObject();
        one.put("approverUid", adapterDeterLab.getDeterUserIdByNclUserId(approver.getId()));
        one.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(userId));
        one.put("pid", pid);
        one.put("gid", pid);
        adapterDeterLab.approveJoinRequest(one.toString());
        teamService.updateMemberStatus(teamId, userId, MemberStatus.APPROVED);
    }

    @Transactional
    public void rejectJoinRequest(String teamId, String userId, User approver) {
        if (!teamService.isOwner(teamId, approver.getId())) {
            log.warn("User {} is not a team owner of Team {}", userId, teamId);
            throw new UserIsNotTeamOwnerException();
        }

        Team one = teamService.getTeamById(teamId);
        String pid = one.getName();
        List<? extends TeamMember> membersList = one.getMembers();

        if (membersList.isEmpty()) {
            // by right not possible to be empty since isOwner ensures there exists at least one member of type owner
            throw new NoMembersInTeamException();
        }

        for (TeamMember member : membersList) {
            if (member.getUserId().equals(userId)) {
                log.info("Reject join request from User {}, Team {}", member.getUserId(), teamId);
                userService.removeTeam(userId, teamId);
                teamService.removeMember(teamId, member);
                // FIXME call adapter deterlab
                JSONObject object = new JSONObject();
                object.put("approverUid", adapterDeterLab.getDeterUserIdByNclUserId(approver.getId()));
                object.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(userId));
                object.put("pid", pid);
                object.put("gid", pid);
                adapterDeterLab.rejectJoinRequest(object.toString());
            }
        }
    }

    @Transactional
    public void approveTeam(String teamId, TeamStatus status) {
        // FIXME required additional parameters to validate if approver is of admin or ordinary user
        if (teamId == null || teamId.isEmpty()) {
            log.warn("Team Id is empty or null");
            throw new RegisterTeamIdEmptyException();
        }

        // change team status
        // invoked method already ensure there is at least a team member of type owner
        Team team = teamService.updateTeamStatus(teamId, status);

        // change team owner member status
        List<? extends TeamMember> membersList = team.getMembers();

        if (membersList.isEmpty()) {
            // paranoid check, just in case
            throw new NoMembersInTeamException();
        }

        for (TeamMember teamMember : membersList) {
            if (teamMember.getMemberType().equals(MemberType.OWNER)) {
                teamService.updateMemberStatus(teamId, teamMember.getUserId(), MemberStatus.APPROVED);
            }
        }

        // FIXME adapter deterlab call here
        JSONObject one = new JSONObject();
        one.put("pid", team.getName());

        if (status.equals(TeamStatus.APPROVED)) {
            adapterDeterLab.approveProject(one.toString());
        } else {
            // FIXME may need to be more specific and check if TeamStatus is REJECTED
            Team existingTeam = teamService.getTeamById(teamId);
            List<? extends TeamMember> existingMembersList = existingTeam.getMembers();
            for (TeamMember member : existingMembersList) {
                // remove from user side
                userService.removeTeam(member.getUserId(), teamId);
            }
            // remove from team side
            teamService.removeTeam(teamId);
            adapterDeterLab.rejectProject(one.toString());
        }
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
        adapterDeterLab.saveDeterUserIdMapping(deterUserId, nclUserId);
    }

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
            log.info("New team name is {}", teamName);
            one = teamService.getTeamByName(teamName);
        } catch (NullPointerException e) {
            log.info("This is good, this implies team name is unique");
        }
        if (one != null && one.getId() != null) {
            if (!one.getId().isEmpty()) {
                log.warn("Team name duplicate entry found");
                throw new RegisterTeamNameDuplicateException();
            }
        }
    }

}
