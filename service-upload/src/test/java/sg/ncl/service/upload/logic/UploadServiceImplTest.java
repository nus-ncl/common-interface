package sg.ncl.service.upload.logic;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.upload.AbstractTest;
import sg.ncl.service.upload.DirectoryProperties;
import sg.ncl.service.upload.data.ResumableEntity;
import sg.ncl.service.upload.data.ResumableStorage;
import sg.ncl.service.upload.domain.UploadService;
import sg.ncl.service.upload.domain.UploadStatus;
import sg.ncl.service.upload.util.TestUtil;
import sg.ncl.service.upload.web.ResumableInfo;

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
    @Mock
    private Claims claims;

    private UploadService uploadService;

    @Before
    public void before() {
        assertThat(mockingDetails(storage).isMock()).isTrue();
        assertThat(mockingDetails(properties).isMock()).isTrue();
        uploadService = new UploadServiceImpl(storage, properties);
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
    public void testAddChunkBadRequestException() {
        ResumableInfo resumableInfo = TestUtil.getResumableInfo();
        int resumableChunkNumber = (new Random()).nextInt();

        final ResumableEntity entity = new ResumableEntity();
        entity.resumableChunkSize = 1;
        entity.resumableTotalSize = 1L;
        entity.resumableIdentifier = RandomStringUtils.randomAlphanumeric(20);
        entity.resumableFilename = RandomStringUtils.randomAlphanumeric(20);
        entity.resumableRelativePath = RandomStringUtils.randomAlphanumeric(20);

        when(properties.getBaseDir()).thenReturn("uploads");
        when(storage.get(anyInt(), anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(entity);

        exception.expect(BadRequestException.class);
        uploadService.addChunk(resumableInfo, resumableChunkNumber, null, null);
    }

}
