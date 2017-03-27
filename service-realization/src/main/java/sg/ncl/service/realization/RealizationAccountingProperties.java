package sg.ncl.service.realization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @Author Tran Ly Vu
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ncl.accounting")
public class RealizationAccountingProperties {
        private String charges;
}
