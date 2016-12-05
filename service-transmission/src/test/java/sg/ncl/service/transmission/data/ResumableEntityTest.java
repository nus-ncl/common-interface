package sg.ncl.service.transmission.data;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by huangxian on 4/12/16.
 */
public class ResumableEntityTest {

    @Test
    public void testGetResumableChunkSize() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableChunkSize()).isNull();
    }

    @Test
    public void testGetResumableTotalSize() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableTotalSize()).isNull();
    }

    @Test
    public void testGetResumableIdentifier() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableIdentifier()).isNull();
    }

    @Test
    public void testGetResumableFilename() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableFilename()).isNull();
    }

    @Test
    public void testGetResumableRelativePath() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableRelativePath()).isNull();
    }

    @Test
    public void testGetResumableChunkNumber() {
        final int number = (new Random()).nextInt();
        final ResumableEntity.ResumableChunkNumber chunkNumber =
                new ResumableEntity.ResumableChunkNumber(number);
        assertThat(chunkNumber.getNumber()).isEqualTo(number);
    }

    @Test
    public void testEqualResumableChunkNumber() {
        final int number = (new Random()).nextInt();
        final ResumableEntity.ResumableChunkNumber chunkNumber1 =
                new ResumableEntity.ResumableChunkNumber(number);
        final ResumableEntity.ResumableChunkNumber chunkNumber2 =
                new ResumableEntity.ResumableChunkNumber(number);
        assertThat(chunkNumber1.equals(chunkNumber2)).isTrue();
    }

    @Test
    public void testHashCodeResumableChunkNumber() {
        final int number = (new Random()).nextInt();
        final ResumableEntity.ResumableChunkNumber chunkNumber =
                new ResumableEntity.ResumableChunkNumber(number);
        assertThat(chunkNumber.hashCode()).isEqualTo(number);
    }

    @Test
    public void testGetResumableFilePath() {
        final ResumableEntity entity = new ResumableEntity();
        assertThat(entity.getResumableFilePath()).isNull();
    }

    @Test
    public void testInvalidResumableEntity() {
        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(-1);
        assertThat(entity.valid()).isFalse();
    }

    @Test
    public void testValidResumableEntity() {
        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(1);
        entity.setResumableTotalSize(1L);
        entity.setResumableIdentifier(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableFilename(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableRelativePath(RandomStringUtils.randomAlphanumeric(20));
        assertThat(entity.valid()).isTrue();
    }

    @Test
    public void testCheckIfUploaoFinishedFalse() {
        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(1);
        entity.setResumableTotalSize(2L);
        assertThat(entity.checkIfUploadFinished()).isFalse();
    }

    @Test
    public void testCheckIfUploaoFinishedTrue() {
        final ResumableEntity entity = new ResumableEntity();
        final int number = (new Random()).nextInt();
        final ResumableEntity.ResumableChunkNumber chunkNumber =
                new ResumableEntity.ResumableChunkNumber(number);

        entity.setResumableChunkSize(2);
        entity.setResumableTotalSize(2L);
        entity.setResumableFilePath("filePDF.temp");

        assertThat(entity.checkIfUploadFinished()).isTrue();
    }

}
