package com.lsm.batch.application.dormant;

import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemReader;

@Component
public class PreDormantBatchItemReader implements ItemReader<Customer> {

	@Override
	public Customer read() {
		return null;
	}
}
