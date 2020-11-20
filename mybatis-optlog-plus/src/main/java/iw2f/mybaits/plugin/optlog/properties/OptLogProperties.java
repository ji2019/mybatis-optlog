package iw2f.mybaits.plugin.optlog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "optlog")
public class OptLogProperties {
	/**
	 * 是否开启
	 */
    private String enable;
}
