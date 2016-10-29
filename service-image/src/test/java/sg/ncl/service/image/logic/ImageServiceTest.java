package sg.ncl.service.image.logic;

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

    private ImageService imageService;

    @Before
    public void before() {
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(imageRepository).isMock()).isTrue();

        imageService = new ImageServiceImpl(adapterDeterLab, imageRepository);
    }

    @Test
    public void testAddImageGood() {
        final Image imageInfo = new ImageInfo(1L, "teamId", "imageName", "nodeId", "description", ImageVisibility.PRIVATE);

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(i -> i.getArgumentAt(0, ImageEntity.class));

        final Image image = imageService.addImage(imageInfo);

        verify(imageRepository, times(1)).save(any(ImageEntity.class));
        assertThat(image.getTeamId()).isEqualTo(imageInfo.getTeamId());
        assertThat(image.getImageName()).isEqualTo(imageInfo.getImageName());
        assertThat(image.getNodeId()).isEqualTo(imageInfo.getNodeId());
        assertThat(image.getDescription()).isEqualTo(imageInfo.getDescription());
        assertThat(image.getVisibility()).isEqualTo(imageInfo.getVisibility());
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
    }

    @Test
    public void testGetListOfImagesGood() {
        List<ImageEntity> expected = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        final ImageEntity entity2 = getImageEntity();
        expected.add(entity1);
        expected.add(entity2);

        when(imageRepository.findAll()).thenReturn(expected);

        List<Image> result = imageService.getAll();
        assertThat(expected).isEqualTo(result);
    }

}
