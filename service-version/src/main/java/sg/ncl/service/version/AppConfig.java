package sg.ncl.service.version;

import org.springframework.context.annotation.Configuration;
import sg.ncl.service.version.domain.VersionInfo;

import javax.inject.Named;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.version.AppConfig")
public class AppConfig {

    @Named
    VersionInfo versionInfo() {
        return new VersionInfo(1, 0, "3ade5", ZonedDateTime.now());
    }

}
