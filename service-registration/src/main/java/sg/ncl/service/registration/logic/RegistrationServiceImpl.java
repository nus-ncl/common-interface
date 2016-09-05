package sg.ncl.service.registration.logic;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;
import sg.ncl.common.DomainProperties;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.web.CredentialsInfo;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.IdNullOrEmptyException;
import sg.ncl.service.registration.exceptions.InvalidTeamStatusException;
import sg.ncl.service.registration.exceptions.NoMembersInTeamException;
import sg.ncl.service.registration.exceptions.RegisterTeamNameDuplicateException;
import sg.ncl.service.registration.exceptions.RegisterTeamNameEmptyException;
import sg.ncl.service.registration.exceptions.RegisterUidNullException;
import sg.ncl.service.registration.exceptions.UserFormException;
import sg.ncl.service.registration.exceptions.UserIsNotTeamMemberException;
import sg.ncl.service.registration.exceptions.UserIsNotTeamOwnerException;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final MailService mailService;
    private final AdapterDeterLab adapterDeterLab;
    private final Template emailValidationTemplate;
    private final DomainProperties domainProperties;

    @Inject
    RegistrationServiceImpl(
            @NotNull final CredentialsService credentialsService,
            @NotNull final TeamService teamService,
            @NotNull final UserService userService,
            @NotNull final RegistrationRepository registrationRepository,
            @NotNull final AdapterDeterLab adapterDeterLab,
            @NotNull final MailService mailService,
            @NotNull final DomainProperties domainProperties,
            @NotNull @Named("emailValidationTemplate") final Template emailValidationTemplate
    ) {
        this.credentialsService = credentialsService;
        this.teamService = teamService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.mailService = mailService;
        this.domainProperties = domainProperties;
        this.emailValidationTemplate = emailValidationTemplate;
    }

    @Override
    @Transactional
    // FIXME: the return type should be a proper Registration
    public Registration registerRequestToApplyTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Team name is empty or null");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            log.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        try {
            userService.getUser(nclUserId);
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

        JSONObject mainObject = new JSONObject();
        mainObject.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(nclUserId));
        mainObject.put("projName", team.getName());
        mainObject.put("pid", team.getName());
        mainObject.put("projGoals", team.getDescription());
        mainObject.put("projWeb", team.getWebsite());
        mainObject.put("projOrg", team.getOrganisationType());
        mainObject.put("projPublic", team.getVisibility());

        log.info("User side add team");
        userService.addTeam(nclUserId, createdTeam.getId());
        teamService.addMember(createdTeam.getId(), teamMemberInfo);

        adapterDeterLab.applyProject(mainObject.toString());
        return null;
    }

    @Override
    @Transactional
    public Registration registerRequestToJoinTeam(String nclUserId, Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Team name is not found");
            throw new RegisterTeamNameEmptyException();
        }

        if (nclUserId == null || nclUserId.isEmpty()) {
            log.warn("Uid is empty or null");
            throw new RegisterUidNullException();
        }

        log.info("Getting the team entity to be join");
        Team teamEntity = teamService.getTeamByName(team.getName());

        if (teamEntity == null) {
            throw new TeamNotFoundException();
        }

        log.info("Team to join: {}", teamEntity.getName());
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

        log.info("Calling the adapter to join project");
        adapterDeterLab.joinProject(userObject.toString());

        log.info("Submitted join team request");
        return null;
    }

    @Override
    @Transactional
    public Registration register(Credentials credentials, User user, Team team, boolean isJoinTeam) {

        if (userFormFieldsHasErrors(user)) {
            log.warn("User form fields has errors {}", user);
            throw new UserFormException();
        }

        if (credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            log.warn("Credentials password is empty");
            throw new UserFormException();
        }

        if (isJoinTeam && (team.getId() == null || team.getId().isEmpty())) {
            log.warn("Team id from join existing team is empty");
            throw new UserFormException();
        }

        if (!isJoinTeam && (team.getName() != null || !team.getName().isEmpty())) {
            Team teamEntity = new TeamEntity();
            try {
                log.info("New team name is {}", team.getName());
                teamEntity = teamService.getTeamByName(team.getName());
            } catch (TeamNotFoundException e) {
                // in order to continue the registration must catch instead of letting the service to throw
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

        if (isJoinTeam) {
            // accept the team data
            teamEntity = teamService.getTeamById(team.getId());
            teamId = team.getId();
            log.info("Register new users: join Team {}", team.getName());
        } else {
            log.info("Creating a team: {}", team);
            // apply for new team
            // check if team already exists
            teamEntity = teamService.createTeam(team);
            teamId = teamEntity.getId();
            log.info("Register new users: apply new Team {}", team.getName());
        }

        // accept user data from form
        User createdUser = userService.createUser(user);
        String userId = createdUser.getId();

        // create the credentials after creating the users
        final CredentialsInfo credentialsInfo = new CredentialsInfo(userId, credentials.getUsername(), credentials.getPassword(), null);
        credentialsService.addCredentials(credentialsInfo);
        log.info("Register new users: create new credentials", credentials.getUsername());

        if (isJoinTeam) {
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

        log.info("Register new users: adding user {} to team {}", user.getUserDetails().getEmail(), team.getName());
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

        if (isJoinTeam) {

            // call python script (create a new user in deterlab)
            // parse in a the json string
            userObject.put("pid", teamEntity.getName());
            resultJSON = adapterDeterLab.joinProjectNewUsers(userObject.toString());
            log.info("Register new users: invoke adapter deterlab to join team {} with data {}", teamEntity.getName(), userObject);

        } else {
            // call python script to apply for new project
            userObject.put("projName", teamEntity.getName());
            userObject.put("projGoals", teamEntity.getDescription());
            userObject.put("pid", teamEntity.getName());
            userObject.put("projWeb", "http://www.nus.edu.sg");
            userObject.put("projOrg", "Academic");
            userObject.put("projPublic", teamEntity.getVisibility());
            resultJSON = adapterDeterLab.applyProjectNewUsers(userObject.toString());
            log.info("Register new users: invoke adapter deterlab to apple new team {} with data {}", teamEntity.getName(), userObject);
        }

        if ("user is created".equals(getUserCreationStatus(resultJSON))) {
            // store form fields into registration repository for recreation when required
            Registration one = addUserToRegistrationRepository(resultJSON, user, teamEntity);

            // call deterlab adapter to store ncluid to deteruid mapping
            addNclUserIdMapping(resultJSON, userId);

            // send verification email
            sendVerificationEmail(createdUser);

            return one;
        } else {
            log.warn("Register new users: unreachable branch, result of registration is {}", resultJSON);
        }
        return null;
    }

    @Override
    @Transactional
    public String approveJoinRequest(String teamId, String userId, User approver) {
        if (!teamService.isOwner(teamId, approver.getId())) {
            log.warn("User {} is not a team owner of Team {}", approver.getId(), teamId);
            throw new UserIsNotTeamOwnerException();
        }
        Team team = teamService.getTeamById(teamId);
        if(team == null) {
            log.warn("Team NOT found, TeamId {}", teamId);
            throw new TeamNotFoundException(teamId);
        }
        String pid = team.getName();
        // already add to user side when request to join
        JSONObject one = new JSONObject();
        one.put("approverUid", adapterDeterLab.getDeterUserIdByNclUserId(approver.getId()));
        one.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(userId));
        one.put("pid", pid);
        one.put("gid", pid);
        one.put("action", "approve");
        User user = userService.getUser(userId);
        if((UserStatus.PENDING).equals(user.getStatus())) {
            userService.updateUserStatus(userId, UserStatus.APPROVED);
        }
        teamService.updateMemberStatus(teamId, userId, MemberStatus.APPROVED);
        return adapterDeterLab.processJoinRequest(one.toString());
    }

    @Override
    @Transactional
    public String rejectJoinRequest(String teamId, String userId, User approver) {
        if (!teamService.isOwner(teamId, approver.getId())) {
            log.warn("User {} is not a team owner of Team {}", approver.getId(), teamId);
            throw new UserIsNotTeamOwnerException();
        }
        Team one = teamService.getTeamById(teamId);
        if(one == null) {
            log.warn("Team NOT found, TeamId {}", teamId);
            throw new TeamNotFoundException(teamId);
        }
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
                object.put("action", "deny");
                return adapterDeterLab.processJoinRequest(object.toString());
            }
        }
        log.warn("Cannot process join request from User {} to Team {}: User is NOT a member of the team.", userId, teamId);
        throw new UserIsNotTeamMemberException();
    }

    @Override
    @Transactional
    public String approveOrRejectNewTeam(
            final String teamId,
            final String ownerId,
            final TeamStatus status
    ) {
        // FIXME required additional parameters to validate if approver is of admin or ordinary user

        if (teamId == null || teamId.isEmpty() || ownerId == null || ownerId.isEmpty()) {
            log.warn("Id null or empty exception. TeamId: {}, UserId: {}", teamId, ownerId);
            throw new IdNullOrEmptyException();
        }
        if (status == null ||
                !(status.equals(TeamStatus.APPROVED) || status.equals(TeamStatus.REJECTED))) {
            log.warn("Invalid TeamStatus {}", status);
            throw new InvalidTeamStatusException();
        }
        // change team status
        // invoked method already ensure there is at least a team member of type owner
        Team team = teamService.updateTeamStatus(teamId, status);

        // FIXME adapter deterlab call here
        JSONObject one = new JSONObject();
        one.put("pid", team.getName());
        one.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(ownerId));

        if (status.equals(TeamStatus.APPROVED)) {
            User user = userService.getUser(ownerId);
            if((UserStatus.PENDING).equals(user.getStatus())) {
                userService.updateUserStatus(ownerId, UserStatus.APPROVED);
            }
            // change team owner member status
            teamService.updateMemberStatus(teamId, ownerId, MemberStatus.APPROVED);
            return adapterDeterLab.approveProject(one.toString());
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
            return adapterDeterLab.rejectProject(one.toString());
        }
    }

    private boolean userFormFieldsHasErrors(User user) {
        boolean errorsFound = false;

        if (user == null) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getFirstName() == null || user.getUserDetails().getFirstName().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getLastName() == null || user.getUserDetails().getLastName().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getJobTitle() == null || user.getUserDetails().getJobTitle().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getEmail() == null || user.getUserDetails().getEmail().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getPhone() == null || user.getUserDetails().getPhone().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getInstitution() == null || user.getUserDetails().getInstitution().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getInstitutionAbbreviation() == null || user.getUserDetails().getInstitutionAbbreviation().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getInstitutionWeb() == null || user.getUserDetails().getInstitutionWeb().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getAddress().getAddress1() == null || user.getUserDetails().getAddress().getAddress1().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getAddress().getCountry() == null || user.getUserDetails().getAddress().getCountry().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getAddress().getRegion() == null || user.getUserDetails().getAddress().getRegion().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getAddress().getCity() == null || user.getUserDetails().getAddress().getCity().isEmpty())) {
            errorsFound = true;
        }

        if (!errorsFound && (user.getUserDetails().getAddress().getZipCode() == null || user.getUserDetails().getAddress().getZipCode().isEmpty())) {
            errorsFound = true;
        }

        return errorsFound;
    }

    @Override
    @Transactional
    public String getDeterUid(String id) {
        return adapterDeterLab.getDeterUserIdByNclUserId(id);
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
        log.info("Register new users: map and save ncl user id: {} to deter user id: {}", nclUserId, deterUserId);
    }

    private Registration addUserToRegistrationRepository(String resultJSON, User user, Team team) {
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

        RegistrationEntity one = registrationRepository.save(registrationEntity);
        log.info("Register new users: saving registration entity {}", one);
        return one;
    }

    private void checkTeamNameDuplicate(String teamName) {
        log.info("New team name is {}", teamName);
        final Team one = teamService.getTeamByName(teamName);
        if (one != null) {
            log.warn("Team name duplicate entry found");
            throw new RegisterTeamNameDuplicateException();
        }
    }

    private void sendVerificationEmail(User user) {
        final Map<String, String> map = new HashMap<>();
        map.put("firstname", user.getUserDetails().getFirstName());
        map.put("domain", domainProperties.getDomain());
        map.put("id", user.getId());
        map.put("email", user.getUserDetails().getEmail());
        map.put("key", user.getVerificationKey());

        /*
         * If sending email fails, we catch the exceptions and log them,
         * rather than throw the exceptions. Hence, the email will not cause
         * the main application to fail. If users cannot receive emails after
         * a certain amount of time, they should send email to support@ncl.sg
         */
        try {
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(
                    emailValidationTemplate, map);
            mailService.send("testbed-ops@ncl.sg",
                    user.getUserDetails().getEmail(),
                    "Please Verify Your Email Account", msgText, false, null, null);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }

    }

}
