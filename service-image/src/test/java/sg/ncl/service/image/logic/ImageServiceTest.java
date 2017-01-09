package sg.ncl.service.image.logic;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.image.data.jpa.ImageEntity;
import sg.ncl.service.image.data.jpa.ImageRepository;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageService;
import sg.ncl.service.image.domain.ImageVisibility;
import sg.ncl.service.image.web.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import static sg.ncl.service.image.util.TestUtil.getImageEntity;

/**
 * Created by dcsyeoty on 29-Oct-16.
 */
public class ImageServiceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private AdapterDeterLab adapterDeterLab;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private Claims claims;

    private ImageService imageService;

    @Before
    public void before() {
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(imageRepository).isMock()).isTrue();
        assertThat(mockingDetails(claims).isMock()).isTrue();

        imageService = new ImageServiceImpl(adapterDeterLab, imageRepository);
    }

    @Test
    public void testAddImageGood() {
        final Image imageInfo = new ImageInfo(1L, "teamId", "imageName", "nodeId", "description", "currentOS", ImageVisibility.PRIVATE);

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(i -> i.getArgumentAt(0, ImageEntity.class));

        final Image image = imageService.addImage(imageInfo, claims);

        verify(imageRepository, times(1)).save(any(ImageEntity.class));
        assertThat(image.getTeamId()).isEqualTo(imageInfo.getTeamId());
        assertThat(image.getImageName()).isEqualTo(imageInfo.getImageName());
        assertThat(image.getNodeId()).isEqualTo(imageInfo.getNodeId());
        assertThat(image.getDescription()).isEqualTo(imageInfo.getDescription());
        assertThat(image.getVisibility()).isEqualTo(imageInfo.getVisibility());
        assertThat(image.getCurrentOS()).isEqualTo(imageInfo.getCurrentOS());
    }

    @Test
    public void testGetImageGood() {
        final ImageEntity entity = getImageEntity();

        when(imageRepository.findOne(eq(entity.getId()))).thenReturn(entity);

        final Image result = imageService.getImage(entity.getId());
        assertThat(entity.getId()).isEqualTo(result.getId());
        assertThat(entity.getTeamId()).isEqualTo(result.getTeamId());
        assertThat(entity.getImageName()).isEqualTo(result.getImageName());
        assertThat(entity.getNodeId()).isEqualTo(result.getNodeId());
        assertThat(entity.getDescription()).isEqualTo(result.getDescription());
        assertThat(entity.getVisibility()).isEqualTo(result.getVisibility());
        assertThat(entity.getCurrentOS()).isEqualTo(result.getCurrentOS());
    }

    @Test
    public void testGetListOfImagesGood() {
        List<ImageEntity> expected = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        final ImageEntity entity2 = getImageEntity();
        expected.add(entity1);
        expected.add(entity2);

        when(imageRepository.findAll()).thenReturn(expected);

        List<Image> result = imageService.getAll(null, null);
        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testGetListOfImageWithTeamIdAndVisibility() {
        List<ImageEntity> expected = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        expected.add(entity1);

        when(imageRepository.findByTeamIdAndVisibility(anyString(), any(ImageVisibility.class))).thenReturn(expected);

        List<Image> result = imageService.getAll(entity1.getTeamId(), entity1.getVisibility());
        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testGetListOfImageWithTeamIdOnly() {
        List<ImageEntity> expected = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        expected.add(entity1);

        when(imageRepository.findByTeamIdOrVisibility(anyString(), any())).thenReturn(expected);

        List<Image> result = imageService.getAll(entity1.getTeamId(), null);
        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testGetListOfImageWithVisibilityOnly() {
        List<ImageEntity> expected = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        final ImageEntity entity2 = getImageEntity();
        expected.add(entity1);
        expected.add(entity2);

        when(imageRepository.findByTeamIdOrVisibility(anyString(), any(ImageVisibility.class))).thenReturn(expected);

        List<Image> result = imageService.getAll(null, ImageVisibility.PRIVATE);
        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testGetGlobalImages() {
        when(adapterDeterLab.getGlobalImages()).thenReturn("adapterResult");

        Map<String, String> result = imageService.getGlobalImages();
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("images", "adapterResult");
    }

    @Test
    public void testGetSavedImages() {

        when(adapterDeterLab.getSavedImages(anyString())).thenReturn("adapterResult");

        Map<String, String> result = imageService.getSavedImages("teamId");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("teamId", "adapterResult");
    }
}
