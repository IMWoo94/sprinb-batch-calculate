package com.lsm.batch.application.dormant;

import org.springframework.stereotype.Component;

import com.lsm.batch.EmailProvider;
import com.lsm.batch.customer.Customer;
import com.lsm.batch.customer.CustomerRepository;
import com.lsm.batch.dormantbatch.ItemWriter;

@Component
public class DormantBatchItemWriter implements ItemWriter<Customer> {

	private final CustomerRepository customerRepository;
	private final EmailProvider emailProvider;

	public DormantBatchItemWriter(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		this.emailProvider = new EmailProvider.Fake();
	}

	@Override
	public void write(Customer customer) {
		// 3. 휴면계정으로 상태를 변경한다.
		customerRepository.save(customer);

		// 4. 메일을 보낸다.
		emailProvider.send(customer.getEmail(), "휴면전환 안내메일입니다.", "휴면 전환");
	}
}
