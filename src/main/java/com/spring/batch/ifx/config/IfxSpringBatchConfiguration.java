package com.spring.batch.ifx.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.batch.ifx.dto.HotelPropertyDetails;
import com.spring.batch.ifx.entities.HotelProperty;
import com.spring.batch.ifx.processor.HotelPropertyProcessor;
import com.spring.batch.ifx.queryprovider.IfxSqlPagingQueryProvider;
import com.spring.batch.ifx.rowmapper.HotelPropertyRowMapper;
import com.spring.batch.ifx.writer.ConsoleItemWriter;

@Configuration
@EnableBatchProcessing
@EnableCaching
public class IfxSpringBatchConfiguration extends DefaultBatchConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(IfxSpringBatchConfiguration.class);

	@Autowired
	@Qualifier("ifxDataSource")
	DataSource ifxDatasource;

	@Autowired
	@Qualifier("ifxTransactionManager")
	PlatformTransactionManager txManager;

	@Override
	public void setDataSource(DataSource dataSource) {
		// We don't want the data source to be configured by Spring Batch
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}

	@Bean("readHotelPropertiesJob")
	public Job readHotelPropertiesJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("readHotelPropertiesJob").incrementer(new RunIdIncrementer())
				.start(propertyReadProcessWriteStep(stepBuilderFactory)).build();
	}

	@Bean("propertyReadProcessWriteStep")
	public Step propertyReadProcessWriteStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("propertyReadProcessWriteStep").<HotelProperty, HotelPropertyDetails>chunk(10000)
				.reader(propertyIfxDBReader(null)).processor(propertyDataProcessor()).writer(propertyStatusBookWriter())
				.taskExecutor(new SimpleAsyncTaskExecutor()).build();
	}

	@Bean("propertyStatusReader")
	@StepScope
	public ItemReader<HotelProperty> propertyIfxDBReader(@Qualifier("ifxDataSource") DataSource ifxDatasource) {
		JdbcPagingItemReader<HotelProperty> jdbcItemReader = new JdbcPagingItemReader<>();
		jdbcItemReader.setQueryProvider(createQueryProvider());
		jdbcItemReader.setDataSource(ifxDatasource);
		jdbcItemReader.setPageSize(100);
		jdbcItemReader.setRowMapper(new HotelPropertyRowMapper());
		return jdbcItemReader;
	}

	private PagingQueryProvider createQueryProvider() {
		IfxSqlPagingQueryProvider queryProvider = new IfxSqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT brand, pid, destination, language");
		queryProvider.setFromClause("FROM hotel_property");
		queryProvider.setWhereClause("WHERE language='en'");
		queryProvider.setSortKeys(sortByBrandAsc());
		return queryProvider;
	}

	private Map<String, Order> sortByBrandAsc() {
		Map<String, Order> sortConfiguration = new HashMap<>();
		sortConfiguration.put("brand", Order.ASCENDING);
		sortConfiguration.put("pid", Order.ASCENDING);
		return sortConfiguration;
	}

	@Bean
	public ItemProcessor<HotelProperty, HotelPropertyDetails> propertyDataProcessor() {
		return new HotelPropertyProcessor();
	}

	@Bean
	public ItemWriter<HotelPropertyDetails> propertyStatusBookWriter() {
		return new ConsoleItemWriter();
	}
}
