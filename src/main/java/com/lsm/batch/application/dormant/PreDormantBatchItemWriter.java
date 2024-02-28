package com.lsm.batch.application.dormant;

import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemWriter;

@Component
public class PreDormantBatchItemWriter implements ItemWriter<Customer> {

	@Override
	public void write(Customer item) {

	}
}
