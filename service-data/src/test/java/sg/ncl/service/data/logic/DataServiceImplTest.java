package sg.ncl.service.data.logic;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.data.AbstractTest;
import sg.ncl.service.data.data.jpa.DataRepository;
import sg.ncl.service.data.domain.DataService;
import sg.ncl.service.data.domain.DataVisibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by jng on 17/10/16.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class DataServiceImplTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataRepository dataRepository;
    @Mock
    private Claims claims;

    private DataService dataService;

    @Before
    public void before() {
        assertThat(mockingDetails(dataRepository).isMock()).isTrue();
        dataService = new DataServiceImpl(dataRepository);
    }

    @Test
    public void testGetDataSetsPublic() {
        dataService.findByVisibility(DataVisibility.PUBLIC);
        verify(dataRepository, times(1)).findByVisibility(any(DataVisibility.class));
    }

    @Test
    public void testGetDataSetsAll() {
        dataService.getAll();
        verify(dataRepository, times(1)).findAll();
    }

}
