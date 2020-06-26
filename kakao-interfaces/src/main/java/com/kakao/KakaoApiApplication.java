package com.kakao;

import com.kakao.configuration.ApiConfig;
import com.kakao.configuration.DomainConfig;
import com.kakao.domain.Domain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication(
	exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class }
)
@ComponentScan(basePackageClasses = { Domain.class })
@Import({
	DomainConfig.class
})
public class KakaoApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(KakaoApiApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean<DispatcherServlet> contextServlet(ApplicationContext parentContext) {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.setBeanName("applicationContext");
		applicationContext.setParent(parentContext);
		applicationContext.register(ApiConfig.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setApplicationContext(applicationContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

		ServletRegistrationBean<DispatcherServlet> contextServlet = new ServletRegistrationBean<>(dispatcherServlet, "/");
		contextServlet.setName("contextServlet");
		return contextServlet;
	}
}
