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
import sg.ncl.service.analytics.data.jpa.ProjectUsageEntity;
import sg.ncl.service.analytics.domain.ProjectDetails;
import sg.ncl.service.analytics.domain.ProjectService;
import sg.ncl.service.analytics.domain.ProjectUsage;
import sg.ncl.service.analytics.exceptions.ProjectDetailsNotFoundException;
import sg.ncl.service.analytics.exceptions.ProjectNameAlreadyExistsException;
import sg.ncl.service.analytics.exceptions.ProjectUsageAlreadyExistsException;
import sg.ncl.service.analytics.exceptions.ProjectUsageNotFoundException;
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

    @Test
    public void testFindProjectUsageByIdNotFound() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        exception.expect(ProjectDetailsNotFoundException.class);
        projectService.findProjectUsageById(detailsEntity.getId(), TestUtil.getProjectUsageIdentity());
    }

    @Test
    public void testFindProjectUsageByIdFound() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        List<ProjectUsageEntity> usageEntityList = new ArrayList<>();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        usageEntityList.add(usageEntity);
        detailsEntity.setProjectUsages(usageEntityList);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        ProjectUsage projectUsage = projectService.findProjectUsageById(detailsEntity.getId(), usageEntity.getId());
        assertThat(projectUsage).isEqualTo(detailsEntity.getProjectUsages().get(0));
    }

    @Test
    public void testFindProjectUsageNull() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        List<ProjectUsageEntity> usageEntityList = new ArrayList<>();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        usageEntityList.add(usageEntity);
        detailsEntity.setProjectUsages(usageEntityList);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        exception.expect(ProjectUsageNotFoundException.class);
        projectService.findProjectUsageById(detailsEntity.getId(), TestUtil.getProjectUsageIdentity());
    }

    @Test
    public void testSaveProjectUsageAlreadyExist() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        detailsEntity.addProjectUsage(usageEntity);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        exception.expect(ProjectUsageAlreadyExistsException.class);
        projectService.createProjectUsage(detailsEntity.getId(), usageEntity);
    }

    @Test
    public void testSaveProjectUsage() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        projectService.createProjectUsage(detailsEntity.getId(), usageEntity);
        verify(projectDetailsRepository, times(1)).save(any(ProjectDetailsEntity.class));
    }

    @Test
    public void testDeleteProjectUsage() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        List<ProjectUsageEntity> usageEntityList = new ArrayList<>();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        usageEntityList.add(usageEntity);
        detailsEntity.setProjectUsages(usageEntityList);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        projectService.deleteProjectUsage(detailsEntity.getId(), usageEntity.getId());
        verify(projectDetailsRepository, times(1)).save(any(ProjectDetailsEntity.class));
    }

    @Test
    public void testUpdateProjectUsage() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        List<ProjectUsageEntity> usageEntityList = new ArrayList<>();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        usageEntityList.add(usageEntity);
        detailsEntity.setProjectUsages(usageEntityList);

        ProjectUsageEntity updatedUsageEntity = TestUtil.getProjectUsageEntity();
        updatedUsageEntity.setId(usageEntity.getId());
        updatedUsageEntity.setMonthlyUsage(usageEntity.getMonthlyUsage() + 1000);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        when(projectDetailsRepository.save(any(ProjectDetailsEntity.class))).thenReturn(detailsEntity);

        ProjectDetails result = projectService.updateProjectUsage(detailsEntity.getId(), updatedUsageEntity);
        verify(projectDetailsRepository, times(1)).save(any(ProjectDetailsEntity.class));
        assertThat(result.getProjectUsages()).isNotEmpty();
        assertThat(result.getProjectUsages().get(0).getMonthlyUsage()).isEqualTo(updatedUsageEntity.getMonthlyUsage());
    }

    @Test
    public void testUpdateProjectUsageNotFound() {
        ProjectDetailsEntity detailsEntity = TestUtil.getProjectDetailsEntity();
        List<ProjectUsageEntity> usageEntityList = new ArrayList<>();
        ProjectUsageEntity usageEntity = TestUtil.getProjectUsageEntity();
        usageEntityList.add(usageEntity);
        detailsEntity.setProjectUsages(usageEntityList);

        ProjectUsageEntity updatedUsageEntity = TestUtil.getProjectUsageEntity();
        updatedUsageEntity.setMonthlyUsage(usageEntity.getMonthlyUsage() + 1000);

        when(projectDetailsRepository.getOne(anyLong())).thenReturn(detailsEntity);
        exception.expect(ProjectUsageNotFoundException.class);
        projectService.updateProjectUsage(detailsEntity.getId(), updatedUsageEntity);
    }
}
