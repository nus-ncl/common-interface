package sg.ncl.service.authentication.domain;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsService {

    List<? extends Credentials> getAll();

    Credentials addCredentials(final Credentials credentials);

    Credentials updateCredentials(final String id, final Credentials credentials);

}
