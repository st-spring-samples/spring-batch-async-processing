package com.sudhirt.practice.batch.async.listener;

import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChunkCountListener extends ChunkListenerSupport {

	@Override
	public void afterChunk(ChunkContext chunkContext) {
		var count = chunkContext.getStepContext().getStepExecution().getWriteCount();
		if (count > 0 && count % 100000 == 0) {
			log.info(count + "records processed");
		}
	}

}
