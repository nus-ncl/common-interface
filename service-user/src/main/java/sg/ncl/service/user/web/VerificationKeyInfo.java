package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Created by dcszwang on 8/12/2016.
 */

@Getter
public class VerificationKeyInfo {

    private final String key;

    @JsonCreator
    public VerificationKeyInfo(@JsonProperty("key") final String key) {
        this.key = key;
    }

    public VerificationKeyInfo(final VerificationKeyInfo keyInfo) {
        this(keyInfo.getKey());
    }

}
