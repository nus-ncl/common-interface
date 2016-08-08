package sg.ncl.service.authentication.domain;

/**
 * @author Christopher Zhong
 */
public interface AuthenticationService {

    Authorization login(String username, String password);

}
