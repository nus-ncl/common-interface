package sg.ncl.service.data.logic;

import freemarker.template.Template;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.data.AbstractTest;
import sg.ncl.service.data.data.jpa.DataAccessRequestEntity;
import sg.ncl.service.data.data.jpa.DataAccessRequestRepository;
import sg.ncl.service.data.data.jpa.DataEntity;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.DataAccessRequestService;
import sg.ncl.service.data.exceptions.DataAccessRequestNotFoundException;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.data.util.TestUtil;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by dcsjnh on 12/22/2016.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class DataAccessRequestServiceImplTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataRepository dataRepository;
    @Mock
    private DataAccessRequestRepository dataAccessRequestRepository;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;
    @Mock
    private DomainProperties domainProperties;
    @Mock
    private Template requestAccessTemplate;
    @Mock
    private Template approvedAccessTemplate;
    @Mock
    private Claims claims;

    private DataAccessRequestService dataAccessRequestService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataRepository).isMock()).isTrue();
        assertThat(mockingDetails(dataAccessRequestRepository).isMock()).isTrue();
        assertThat(mockingDetails(userService).isMock()).isTrue();
        assertThat(mockingDetails(mailService).isMock()).isTrue();
        dataAccessRequestService = new DataAccessRequestServiceImpl(
                dataRepository,
                dataAccessRequestRepository,
                userService,
                mailService,
                domainProperties,
                requestAccessTemplate,
                approvedAccessTemplate);
    }

    @Test
    public void testCreateRequestDataNotFound() {
        when(dataRepository.getOne(anyLong())).thenReturn(null);
        exception.expect(DataNotFoundException.class);
        dataAccessRequestService.createRequest(1L, "", claims);
    }

    @Test
    public void testCreateRequest() throws Exception {
        DataEntity dataEntity = TestUtil.getDataEntity();
        UserEntity userEntity = TestUtil.getUserEntity();
        DataAccessRequestEntity dataAccessRequestEntity = TestUtil.getDataAccessRequestEntity();

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);
        when(dataAccessRequestRepository.findByDataIdAndRequesterId(anyLong(), anyString())).thenReturn(new ArrayList<DataAccessRequestEntity>());
        when(dataAccessRequestRepository.save(any(DataAccessRequestEntity.class))).thenReturn(dataAccessRequestEntity);

        dataAccessRequestService.createRequest(1L, "", claims);
        verify(dataAccessRequestRepository, times(1)).save(any(DataAccessRequestEntity.class));
    }

    @Test
    public void testApproveRequestDataNotFound() {
        when(dataRepository.getOne(anyLong())).thenReturn(null);
        exception.expect(DataNotFoundException.class);
        dataAccessRequestService.approveRequest(1L, 1L, claims);
    }

    @Test
    public void testApproveRequestDataAccessRequestNotFound() {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        when(dataAccessRequestRepository.getOne(anyLong())).thenReturn(null);

        exception.expect(DataAccessRequestNotFoundException.class);
        dataAccessRequestService.approveRequest(1L, 1L, claims);
    }

    @Test
    public void testApproveRequest() throws Exception {
        DataEntity dataEntity = TestUtil.getDataEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());
        DataAccessRequestEntity dataAccessRequestEntity = TestUtil.getDataAccessRequestEntity();
        UserEntity userEntity = TestUtil.getUserEntity();

        when(dataRepository.getOne(anyLong())).thenReturn(dataEntity);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(claims.getSubject()).thenReturn(dataEntity.getContributorId());
        when(dataAccessRequestRepository.getOne(anyLong())).thenReturn(dataAccessRequestEntity);
        when(dataAccessRequestRepository.save(any(DataAccessRequestEntity.class))).thenReturn(dataAccessRequestEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);

        dataAccessRequestService.approveRequest(1L, 1L, claims);
        verify(dataAccessRequestRepository, times(1)).save(any(DataAccessRequestEntity.class));
    }

}
