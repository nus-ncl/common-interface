package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.user.domain.PublicKey;

@Getter
public class PublicKeyInfo implements PublicKey {

    private final String publicKey;
    private final String password;

    @JsonCreator
    public PublicKeyInfo(@JsonProperty("publicKey") final String publicKey, @JsonProperty("password") final String password) {
        this.publicKey = publicKey;
        this.password = password;
    }

    public PublicKeyInfo(final PublicKey key) {
        this(key.getPublicKey(), key.getPassword());
    }
}
