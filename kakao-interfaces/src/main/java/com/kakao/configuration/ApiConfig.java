package com.kakao.configuration;

import com.kakao.interfaces.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan(basePackageClasses = { Api.class })
public class ApiConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(httpRequestHeaderInterceptor());
	}

	@Bean
	public HttpRequestHeaderInterceptor httpRequestHeaderInterceptor() {
		return new HttpRequestHeaderInterceptor();
	}
}
