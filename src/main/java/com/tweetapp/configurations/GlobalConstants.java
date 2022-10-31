package com.tweetapp.configurations;

public class GlobalConstants {

	public static final String QUEUE = "tweeetapp_queue";
	public static final String EXCHANGE = "tweeetapp_exchange";
	public static final String ROUTING_KEY = "tweeetapp_routingKey";

	public static final String SECRET_KEY = "jwtsecret";
	public static final String USER_NOT_FOUND = "Users with username %s are not found in the application";
	public static final String USER_ALREADY_EXIST = "UserName %s is not available.";
	public static final String[] PUBLIC_URLS = { "/v3/api-docs", "/v2/api-docs", "/swagger-resources/**",
			"/swagger-ui/**", "/webjars/**", "/api/v1.0/tweets/login", "/api/v1.0/tweets/register", "/api/v1.0/tweets/forgot/*"};

}
