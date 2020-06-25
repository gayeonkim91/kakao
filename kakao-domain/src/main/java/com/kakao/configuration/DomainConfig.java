package com.kakao.configuration;

import com.kakao.domain.Domain;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
	RedisConfig.class,
	JpaConfig.class
})
@ComponentScan(basePackageClasses = { Domain.class })
public class DomainConfig {
}
