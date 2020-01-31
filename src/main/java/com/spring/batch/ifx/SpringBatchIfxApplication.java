package com.spring.batch.ifx;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchIfxApplication implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier(value = "readHotelPropertiesJob")
	Job job;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchIfxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Started reading hotel properties");
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
		System.out.println("Ended reading hotel properties");
	}
}
