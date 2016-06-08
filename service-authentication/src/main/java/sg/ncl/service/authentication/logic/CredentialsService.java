package sg.ncl.service.authentication.logic;

import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Credentials;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsService {

    List<? extends Credentials> getAll();

    CredentialsEntity addCredentials(final Credentials credentials);

    void updateCredentials(final String id, final Credentials credentials);

}
