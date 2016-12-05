package sg.ncl.service.transmission;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by dcsjnh on 11/23/2016.
 *
 * References:
 * [1] https://dzone.com/articles/spring-boot-configurationproperties-1
 */
@Configuration
@ConfigurationProperties(prefix = "transmission")
@Getter
@Setter
public class DirectoryProperties {

    private String baseDir;
    private Map<String, String> subDirs;

}
