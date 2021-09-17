package consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paths")
public class ConfigProperties {
	
	@Value("${paths.getlabels}")
	private String getLabels;

}
