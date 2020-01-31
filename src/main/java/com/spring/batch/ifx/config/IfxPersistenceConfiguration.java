package com.spring.batch.ifx.config;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"com.spring.batch.ifx.repository" }, entityManagerFactoryRef = "ifxEntityManager", transactionManagerRef = "ifxTransactionManager")
@ComponentScan(basePackages = "com.spring.batch.ifx.repository")
public class IfxPersistenceConfiguration {

	@Bean("ifxDataSource")
	public DataSource ifxDataSource(Environment env) {
		return DataSourceBuilder.create() //
				.url(env.getProperty("spring.ifx.datasource.url"))
				//
				.username(env.getProperty("spring.ifx.datasource.username"))
				//
				.password(env.getProperty("spring.ifx.datasource.password"))
				//
				.driverClassName(env.getProperty("spring.ifx.datasource.driver-class-name"))
				//
				.type(HikariDataSource.class)
				//
				.build();
	}

	@Bean("ifxEntityManager")
	public LocalContainerEntityManagerFactoryBean ifxEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("ifxDataSource") DataSource dataSource) {
		final HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "none");
		properties.put("hibernate.dialect", "org.hibernate.dialect.InformixDialect");
		properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
		properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());

		final LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource)
				.packages("com.spring.batch.ifx.entities").persistenceUnit("ifxPU").build();
		em.setJpaPropertyMap(properties);
		return em;
	}

	@Bean("ifxTransactionManager")
	public PlatformTransactionManager ifxTransactionManager(
			@Qualifier("ifxEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean("ifxJdbcTemplate")
	public JdbcTemplate ifxJdbcTemplate(@Qualifier("ifxDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean("ifxNamedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate ifxNamedParameterJdbcTemplate(
			@Qualifier("ifxDataSource") DataSource configDataSource) {
		return new NamedParameterJdbcTemplate(configDataSource);
	}
}
