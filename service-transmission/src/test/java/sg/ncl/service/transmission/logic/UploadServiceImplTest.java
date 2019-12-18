package sg.ncl.service.transmission.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.transmission.AbstractTest;
import sg.ncl.service.transmission.DirectoryProperties;
import sg.ncl.service.transmission.data.ResumableEntity;
import sg.ncl.service.transmission.data.ResumableStorage;
import sg.ncl.service.transmission.domain.UploadService;
import sg.ncl.service.transmission.domain.UploadStatus;
import sg.ncl.service.transmission.util.TestUtil;
import sg.ncl.service.transmission.web.ResumableInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

/**
 * Created by huangxian on 4/12/16.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class UploadServiceImplTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private ResumableStorage storage;
    @Mock
    private DirectoryProperties properties;

    private UploadService uploadService;
    private String tempFolder;

    @Before
    public void before() {
        assertThat(mockingDetails(storage).isMock()).isTrue();
        assertThat(mockingDetails(properties).isMock()).isTrue();
        uploadService = new UploadServiceImpl(storage, properties);
        tempFolder = "tempUploads";
    }
    
    @After
    public void after() throws IOException {
        Files.deleteIfExists(Paths.get(System.getProperty("user.home"), tempFolder));
    }

    @Test
    public void testDeleteDirectory() {
        when(properties.getBaseDir()).thenReturn(tempFolder);
        uploadService.deleteDirectory(null, null);
    }

    @Test
    public void testDeleteUpload() throws IOException {
        when(properties.getBaseDir()).thenReturn(tempFolder);
        boolean result = uploadService.deleteUpload(null, null, "test.txt");
        assertThat(result).isFalse();
    }

    @Test
    public void testFileTraversalDeleteUpload() throws IOException {
        when(properties.getBaseDir()).thenReturn(tempFolder);
        boolean result = uploadService.deleteUpload(null, null, "../test.txt");
        assertThat(result).isFalse();
    }

    @Test
    public void testCheckChunkUploaded() {
        final ResumableEntity entity = new ResumableEntity();
        final int resumableChunkNumber = (new Random()).nextInt();
        String resumableIdentifier = "identifier";

        HashSet<ResumableEntity.ResumableChunkNumber> hashSet = new HashSet<>();
        hashSet.add(new ResumableEntity.ResumableChunkNumber(resumableChunkNumber));
        entity.uploadedChunks = hashSet;
        when(storage.get(anyString())).thenReturn(entity);

        UploadStatus status = uploadService.checkChunk(resumableIdentifier, resumableChunkNumber);
        assertThat(status).isEqualTo(UploadStatus.UPLOADED);
    }

    @Test
    public void testCheckChunkNotFound() {
        final ResumableEntity entity = new ResumableEntity();
        final int resumableChunkNumber = (new Random()).nextInt();
        String resumableIdentifier = "identifier";

        when(storage.get(anyString())).thenReturn(entity);

        UploadStatus status = uploadService.checkChunk(resumableIdentifier, resumableChunkNumber);
        assertThat(status).isEqualTo(UploadStatus.NOT_FOUND);
    }

    @Test
    public void testAddChunkInvalidBadRequestException() {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        int resumableChunkNumber = (new Random()).nextInt();

        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(-1);
        entity.setResumableTotalSize(1L);
        entity.setResumableIdentifier(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableFilename(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableRelativePath(RandomStringUtils.randomAlphanumeric(20));

        when(properties.getBaseDir()).thenReturn(tempFolder);
        when(storage.get(anyInt(), anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(entity);

        exception.expect(BadRequestException.class);
        uploadService.addChunk(resumableInfo, resumableChunkNumber, null, null);
    }

    @Test
    public void testAddChunkWriteChunkBadRequestException() {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        int resumableChunkNumber = (new Random()).nextInt();

        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(1);
        entity.setResumableTotalSize(1L);
        entity.setResumableIdentifier(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableFilename(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableRelativePath(RandomStringUtils.randomAlphanumeric(20));

        when(properties.getBaseDir()).thenReturn(tempFolder);
        when(storage.get(anyInt(), anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(entity);

        exception.expect(BadRequestException.class);
        uploadService.addChunk(resumableInfo, resumableChunkNumber, null, null);
    }

}
