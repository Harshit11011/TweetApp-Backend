package com.tweetapp.configurations;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.tweetapp.controllers")).paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {

		return new ApiInfo("TweetApp", "This is an imitation of simple functionalities of Twitter", "V1.0",
				"Terms of service", new Contact("Harshit", "dummy website", "Harshit.Sachan@yahoo.com"),
				"Feel Free to use the services ", "Dummy Url", Collections.emptyList());

	}

}
