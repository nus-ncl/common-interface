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
import sg.ncl.service.data.AbstractTest;
import sg.ncl.service.data.data.jpa.DataAccessRequestRepository;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.DataAccessRequestService;
import sg.ncl.service.data.exceptions.DataNotFoundException;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.user.domain.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

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

}
