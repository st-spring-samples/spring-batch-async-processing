package com.sudhirt.practice.batch.async.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import com.sudhirt.practice.batch.async.dto.RideInfoItem;
import com.sudhirt.practice.batch.async.listener.ChunkCountListener;
import com.sudhirt.practice.batch.async.mapper.RideInfoFieldSetMapper;
import com.sudhirt.practice.batch.async.tasklet.CleanupTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	@Value("${thread.count}")
	private Integer threadCount;

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void initialize() {
		if (threadCount == null) {
			threadCount = 1;
		}
	}

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).flow(cleanupStep(stepBuilderFactory))
				.next(processStep(stepBuilderFactory)).end().build();
	}

	@Bean
	public Step cleanupStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("cleanupStep").tasklet(new CleanupTasklet(dataSource)).build();
	}

	@Bean
	public Step processStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("processStep").<RideInfoItem, RideInfoItem>chunk(1000)
				.reader(fileReader(OVERRIDDEN_BY_EXPRESSION)).writer(jdbcWriter()).listener(new ChunkCountListener())
				.taskExecutor(taskExecutor()).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<RideInfoItem> fileReader(@Value("#{jobParameters[pathToFile]}") String pathToFile) {
		return new FlatFileItemReaderBuilder<RideInfoItem>().name("transactionReader")
				.resource(new FileSystemResource(pathToFile)).delimited()
				.names(new String[] { "vendorId", "startTime", "endTime", "passengerCount", "tripDistance", "rateCode",
						"storeAndFwdFlag", "pickupLocationId", "dropoffLocationId", "paymentType", "fareAmount",
						"extra", "mtaTax", "tipAmount", "tollAmount", "improvementSurcharge", "totalAmount",
						"congestionSurcharge" })
				.linesToSkip(1).fieldSetMapper(new RideInfoFieldSetMapper()).build();
	}

	@Bean
	public JdbcBatchItemWriter<RideInfoItem> jdbcWriter() {
		var itemWriter = new JdbcBatchItemWriter<RideInfoItem>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<RideInfoItem>());
		itemWriter.setSql(
				"INSERT INTO trips (vendor_id, start_time, end_time, passenger_count, trip_distance, rate_code, "
						+ "store_fwd_flag, pickup_location_id, dropoff_location_id, payment_type, fare_amount, extra, "
						+ "mta_tax, tip_amount, toll_amount, improvement_surcharge, total_amount, congestion_surcharge) "
						+ "values (:vendorId, :startTime, :endTime, :passengerCount, :tripDistance, :rateCode, "
						+ ":storeAndFwdFlag, :pickupLocationId, :dropoffLocationId, :paymentType, :fareAmount, :extra, "
						+ ":mtaTax, :tipAmount, :tollAmount, :improvementSurcharge, :totalAmount, :congestionSurcharge)");
		return itemWriter;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		var taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(threadCount);
		return taskExecutor;
	}

}