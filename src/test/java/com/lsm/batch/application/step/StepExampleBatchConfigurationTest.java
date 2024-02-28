package com.lsm.batch.application.step;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lsm.batch.dormantbatch.Job;

@SpringBootTest
class StepExampleBatchConfigurationTest {

	@Autowired
	private Job stepExampleBatchJob;

	@Test
	void test() {
		stepExampleBatchJob.execute();
	}

}