package sg.ncl.service.registration.web;

import lombok.Getter;
import lombok.Setter;

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
