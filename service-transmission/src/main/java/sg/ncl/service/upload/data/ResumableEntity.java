package sg.ncl.service.upload.data;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.service.upload.domain.Resumable;
import sg.ncl.service.upload.util.HttpUtils;

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
public class ResumableEntity implements Resumable {

    private Integer  resumableChunkSize;
    private Long     resumableTotalSize;
    private String   resumableIdentifier;
    private String   resumableFilename;
    private String   resumableRelativePath;
    private String resumableFilePath;

    public static class ResumableChunkNumber {
        public ResumableChunkNumber(int number) {
            this.number = number;
        }

        public int number;

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
        //check if upload finished
        int count = (int) Math.ceil(((double) resumableTotalSize) / ((double) resumableChunkSize));
        for (int i = 1; i < count; i ++) {
            if (!uploadedChunks.contains(new ResumableChunkNumber(i))) {
                return false;
            }
        }

        //Upload finished, change filename.
        File file = new File(resumableFilePath);
        String new_path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - ".temp".length());
        file.renameTo(new File(new_path));
        return true;
    }

}
