package sg.ncl.service.registration.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Tran Ly Vu
 */
@Setter
@Getter
public class NewMemberResetPasswordInfo {
    private String firstName;
    private String lastName;
    private String phone;
    private String key;
    private String newPassword;
}
