package sg.ncl.service.user.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Created by dcszwang on 7/24/2018.
 */
@Getter
public class StudentInfo {
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String key;
    private final String password;

    @JsonCreator
    public StudentInfo(
            @JsonProperty("firstName") final String firstName,
            @JsonProperty("lastName") final String lastName,
            @JsonProperty("phone") final String phone,
            @JsonProperty("key") final String key,
            @JsonProperty("password") final String password
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.key = key;
        this.password = password;
    }
}
