package sg.ncl.service.analytics.logic;

import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.analytics.data.jpa.ProjectDetailsEntity;
import sg.ncl.service.analytics.data.jpa.ProjectDetailsRepository;
import sg.ncl.service.analytics.domain.ProjectService;
import sg.ncl.service.analytics.exceptions.ProjectDetailsNotFoundException;
import sg.ncl.service.analytics.exceptions.ProjectNameAlreadyExistsException;
import sg.ncl.service.analytics.util.TestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(JMockit.class)
@TestPropertySource(properties = "flyway.enabled=false")
public class ProjectServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private ProjectDetailsRepository projectDetailsRepository;

    private ProjectService projectService;

    @Before
    public void before() {
        assertThat(mockingDetails(projectDetailsRepository).isMock()).isTrue();
        projectService = new ProjectServiceImpl(projectDetailsRepository);
    }

    @Test
    public void testGetAllProjectDetails() {
        projectService.getAllProjectDetails();
        verify(projectDetailsRepository, times(1)).findAll();
    }

    @Test
    public void testGetProjectDetailsUnknownId() {
        when(projectDetailsRepository.getOne(anyLong())).thenReturn(null);
        exception.expect(ProjectDetailsNotFoundException.class);
        projectService.getProjectDetails(1L);
    }

    @Test
    public void testGetProjectDetailsKnownId() {
        when(projectDetailsRepository.getOne(anyLong())).thenReturn(TestUtil.getProjectDetailsEntity());
        projectService.getProjectDetails(1L);
        verify(projectDetailsRepository, times(1)).getOne(anyLong());
    }

    @Test
    public void testSaveNewProjectDetailsWithExistingName() {
        List<ProjectDetailsEntity> detailsEntityList = new ArrayList<>();
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        detailsEntityList.add(detailsEntity);

        when(projectDetailsRepository.findByProjectName(anyString())).thenReturn(detailsEntityList);
        exception.expect(ProjectNameAlreadyExistsException.class);
        projectService.createProjectDetails(detailsEntity);
    }

    @Test
    public void testSaveNewProjectDetailsWithNewName() {
        List<ProjectDetailsEntity> detailsEntityList = new ArrayList<>();
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        detailsEntityList.add(detailsEntity);

        when(projectDetailsRepository.findByProjectName(anyString())).thenReturn(detailsEntityList);
        projectService.createProjectDetails(TestUtil.getProjectDetailsEntity());
        verify(projectDetailsRepository, times(1)).save(any(ProjectDetailsEntity.class));
    }

    @Test
    public void testSaveUpdatedProjectDetailsGood() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        projectService.updateProjectDetails(detailsEntity.getId(), detailsEntity);
        verify(projectDetailsRepository, times(1)).save(any(ProjectDetailsEntity.class));
    }

    @Test
    public void testDeleteProjectDetailsGood() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        projectService.deleteProjectDetails(detailsEntity.getId());
        verify(projectDetailsRepository, times(1)).delete(anyLong());
    }
}
