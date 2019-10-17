package com.sudhirt.practice.batch.async;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@SpringBatchTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/drop.sql")
public class AsyncBatchDemoApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void launch() throws Exception {
		var jobExecution = jobLauncherTestUtils.launchJob(buildJobParameters());
		assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
	}

	private JobParameters buildJobParameters() {
		return buildJobParameters("src/test/resources/rideinfo.csv");
	}

	private JobParameters buildJobParameters(String filePath) {
		return new JobParametersBuilder().addString("pathToFile", filePath)
				.addLong("currentTimeInMillis", System.currentTimeMillis()).toJobParameters();
	}

}
