package gov.epa.ccte.api.physchem.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry cors) {
		cors
				.addMapping("/**")
				.allowedMethods("HEAD", "GET", "POST");
	}

}
