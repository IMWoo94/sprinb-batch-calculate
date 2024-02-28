package com.lsm.batch.application.dormant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.customer.CustomerRepository;
import com.lsm.batch.dormantbatch.ItemReader;

@Component
public class DormantBatchItemReader implements ItemReader<Customer> {

	private int pageNo = 0;
	private final CustomerRepository customerRepository;

	public DormantBatchItemReader(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Customer read() {
		// 1. 고객 조회
		// 한명의 고객씩 가져오기
		final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
		final Page<Customer> page = customerRepository.findAll(pageRequest);

		if (page.isEmpty()) {
			pageNo = 0;
			return null;
		} else {
			pageNo++;
			return page.getContent().get(0);
		}
	}
}
