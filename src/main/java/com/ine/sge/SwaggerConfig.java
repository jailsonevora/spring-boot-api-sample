package com.ine.sge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ine.sge"))
				.paths(PathSelectors.ant("/api/v1/**"))
				.build()
				.apiInfo(metaData())
				/*.produces(de)
				.consumes(DEFAULT_PRODUCES_AND_CONSUMES)*/;
	}

	private ApiInfo metaData() {
		return new ApiInfo(
				"SGE INE Angola REST API",
				"Sistema de Gestão de Entidades do INE Angola",
				"1.0",
				"TERMS OF SERVICE URL",
				new Contact("Jailson Eduardo Evora", "https://www.linkedin.com/in/jailson-eduardo-evora-09393783/", "jailsonevora@gmail.com"),

				"Apache License Version 2.0",
				"https://www.apache.org/licenses/LICENSE-2.0",
				Collections.emptyList()
		);
	}
}