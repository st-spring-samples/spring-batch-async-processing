package com.sudhirt.practice.batch.async.tasklet;

import javax.sql.DataSource;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class CleanupTasklet implements Tasklet {

	private JdbcTemplate jdbcTemplate;

	public CleanupTasklet(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		jdbcTemplate.update("delete from trips");
		return RepeatStatus.FINISHED;
	}

}