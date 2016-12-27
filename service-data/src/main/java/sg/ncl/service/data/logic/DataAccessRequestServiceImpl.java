package sg.ncl.service.data.logic;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sg.ncl.common.DomainProperties;
import sg.ncl.service.data.data.jpa.DataAccessRequestEntity;
import sg.ncl.service.data.data.jpa.DataAccessRequestRepository;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataAccessRequest;
import sg.ncl.service.data.domain.DataAccessRequestService;
import sg.ncl.service.data.exceptions.DataAccessRequestNotFoundException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.ncl.service.data.validations.Validator.checkPermissions;

/**
 * Created by dcsjnh on 12/22/2016.
 */
@Service
@Slf4j
public class DataAccessRequestServiceImpl implements DataAccessRequestService {

    private static final String DATA_NOT_FOUND = "Data not found.";

    private final DataRepository dataRepository;
    private final DataAccessRequestRepository dataAccessRequestRepository;
    private final UserService userService;
    private final MailService mailService;
    private final DomainProperties domainProperties;
    private final Template requestAccessTemplate;
    private final Template approvedAccessTemplate;

    @Inject
    DataAccessRequestServiceImpl(@NotNull final DataRepository dataRepository,
                                 @NotNull final DataAccessRequestRepository dataAccessRequestRepository,
                                 @NotNull final UserService userService,
                                 @NotNull final MailService mailService,
                                 @NotNull final DomainProperties domainProperties,
                                 @NotNull @Named("requestAccessTemplate") final Template requestAccessTemplate,
                                 @NotNull @Named("approvedAccessTemplate") final Template approvedAccessTemplate) {
        this.dataRepository = dataRepository;
        this.dataAccessRequestRepository = dataAccessRequestRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.domainProperties = domainProperties;
        this.requestAccessTemplate = requestAccessTemplate;
        this.approvedAccessTemplate = approvedAccessTemplate;
    }

    @Override
    public DataAccessRequest createRequest(Long id, String reason, Claims claims) {
        Data data = dataRepository.getOne(id);
        if (data == null) {
            throw new DataNotFoundException(DATA_NOT_FOUND);
        }

        User owner = userService.getUser(data.getContributorId());
        User requester = userService.getUser(claims.getSubject());

        DataAccessRequestEntity newEntity;
        List<DataAccessRequestEntity> list = dataAccessRequestRepository.findByDataIdAndRequesterId(id, requester.getId());
        if (list.isEmpty()) {
            newEntity = new DataAccessRequestEntity();
            newEntity.setDataId(data.getId());
            newEntity.setRequesterId(requester.getId());
        } else {
            newEntity = list.get(0);
        }
        newEntity.setReason(reason);
        newEntity.setRequestDate(ZonedDateTime.now());
        DataAccessRequestEntity savedEntity = dataAccessRequestRepository.save(newEntity);

        final Map<String, String> map = new HashMap<>();
        map.put("firstname", owner.getUserDetails().getFirstName());
        map.put("dataname", data.getName());
        map.put("fullname", requester.getUserDetails().getFirstName() + " " + requester.getUserDetails().getLastName());
        map.put("email", requester.getUserDetails().getEmail());
        map.put("phone", requester.getUserDetails().getPhone());
        map.put("jobtitle", requester.getUserDetails().getJobTitle());
        map.put("institution", requester.getUserDetails().getInstitution());
        map.put("country", requester.getUserDetails().getAddress().getCountry());
        map.put("reason", savedEntity.getReason());
        map.put("domain", domainProperties.getDomain());
        map.put("id", savedEntity.getId().toString());

        try {
            String[] to = new String[1];
            to[0] = owner.getUserDetails().getEmail();
            String[] cc = new String[2];
            cc[0] = requester.getUserDetails().getEmail();
            cc[1] = "support@ncl.sg";
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(requestAccessTemplate, map);
            mailService.send("testbed-ops@ncl.sg", to, "Pending Dataset Access Request", msgText, false, cc, null);
            log.debug("Email sent: {}", msgText);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }

        log.info("Data access request created: {}", savedEntity);
        return savedEntity;
    }

    @Override
    public DataAccessRequest approveRequest(Long rid, Claims claims) {
        DataAccessRequestEntity dataAccessRequestEntity = dataAccessRequestRepository.getOne(rid);
        if (dataAccessRequestEntity == null) {
            throw new DataAccessRequestNotFoundException("Data access request not found.");
        }
        Long did = dataAccessRequestEntity.getDataId();
        Data data = dataRepository.getOne(did);
        if (data == null) {
            throw new DataNotFoundException(DATA_NOT_FOUND);
        }
        checkPermissions(data, claims);

        dataAccessRequestEntity.setApprovedDate(ZonedDateTime.now());
        DataAccessRequestEntity savedEntity = dataAccessRequestRepository.save(dataAccessRequestEntity);

        User owner = userService.getUser(data.getContributorId());
        User requester = userService.getUser(savedEntity.getRequesterId());

        final Map<String, String> map = new HashMap<>();
        map.put("firstname", requester.getUserDetails().getFirstName());
        map.put("dataname", data.getName());

        try {
            String[] to = new String[1];
            to[0] = requester.getUserDetails().getEmail();
            String[] cc = new String[2];
            cc[0] = owner.getUserDetails().getEmail();
            cc[1] = "support@ncl.sg";
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(approvedAccessTemplate, map);
            mailService.send("testbed-ops@ncl.sg", to, "Dataset Access Request Approved", msgText, false, cc, null);
            log.debug("Email sent: {}", msgText);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }

        log.info("Data access request approved: {}", savedEntity);
        return savedEntity;
    }

    @Override
    public DataAccessRequest getRequest(Long rid, Claims claims) {
        DataAccessRequestEntity dataAccessRequestEntity = dataAccessRequestRepository.getOne(rid);
        if (dataAccessRequestEntity == null) {
            throw new DataAccessRequestNotFoundException("Data access request not found.");
        }
        Long did = dataAccessRequestEntity.getDataId();
        Data data = dataRepository.getOne(did);
        if (data == null) {
            throw new DataNotFoundException(DATA_NOT_FOUND);
        }
        checkPermissions(data, claims);
        return dataAccessRequestEntity;
    }

}
