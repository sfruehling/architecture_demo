package de.andrena.architecturedemo.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

	private static final String ORIGIN1 = "http://localhost:4200";
	private static final String ORIGIN2 = "http://localhost:4201";
	private static final String ORIGIN3 = "http://localhost:4202";
	private static final String ORIGIN4 = "http://localhost:4203";
	@Value("${security.cors.dashboard}")
	private String dashboard;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //
				.allowedOrigins(dashboard, ORIGIN1, ORIGIN2, ORIGIN3, ORIGIN4)//
				.allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE") //
				.allowCredentials(true);
	}

}
