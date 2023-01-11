package com.softknife.release;

import com.softknife.release.config.AppProp;
import com.softknife.release.config.SecurityProp;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties({AppProp.class, SecurityProp.class})
@EnableScheduling()
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "Infra API", version = "1.0", description = "Car IQ Infra"))
public class ReleaseNotesApp {

	public static void main(String[] args) {
		SpringApplication.run(ReleaseNotesApp.class, args);
	}

}
