package sg.ncl.service.transmission.data;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.service.transmission.domain.Resumable;
import sg.ncl.service.transmission.util.HttpUtils;

import java.io.File;
import java.util.HashSet;

/**
 * Created by dcsjnh on 11/23/2016.
 *
 * References:
 * [1] https://github.com/23/resumable.js/blob/master/samples/java/src/main/java/resumable/js/upload/ResumableInfo.java
 */
@Getter
@Setter
@Slf4j
public class ResumableEntity implements Resumable {

    private Integer  resumableChunkSize;
    private Long     resumableTotalSize;
    private String   resumableIdentifier;
    private String   resumableFilename;
    private String   resumableRelativePath;
    private String   resumableFilePath;

    public static class ResumableChunkNumber {
        private int number;

        public ResumableChunkNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ResumableChunkNumber && ((ResumableChunkNumber) obj).number == this.number;
        }

        @Override
        public int hashCode() {
            return number;
        }
    }

    //Chunks uploaded
    public HashSet<ResumableChunkNumber> uploadedChunks = new HashSet<ResumableChunkNumber>();

    public boolean valid() {
        return !(resumableChunkSize < 0 || resumableTotalSize < 0 || isEmpty());
    }

    private boolean isEmpty() {
        return HttpUtils.isEmpty(resumableIdentifier)
                || HttpUtils.isEmpty(resumableFilename)
                || HttpUtils.isEmpty(resumableRelativePath);
    }

    public boolean checkIfUploadFinished() {
        //check if transmission finished
        int count = (int) Math.ceil(((double) resumableTotalSize) / ((double) resumableChunkSize));
        for (int i = 1; i < count; i ++) {
            if (!uploadedChunks.contains(new ResumableChunkNumber(i))) {
                return false;
            }
        }

        //Upload finished, change filename.
        File file = new File(resumableFilePath);
        String newPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - ".temp".length());
        boolean success = file.renameTo(new File(newPath));
        if (!success) {
            log.warn("Temp file {} not renamed.", resumableFilePath);
        }
        return true;
    }

}
