package com.kakao;

import com.kakao.configuration.ApiConfig;
import com.kakao.configuration.DomainConfig;
import com.kakao.domain.Domain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
	exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class }
)
@ComponentScan(basePackageClasses = { Domain.class })
@Import({
	DomainConfig.class,
	ApiConfig.class
})
public class KakaoApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(KakaoApiApplication.class, args);
	}
}
