package com.lsm.batch.customer;

import org.springframework.stereotype.Component;

@Component
public class DormantBatchJob {

	private final CustomerRepository customerRepository;
	private final EmailProvider emailProvider;

	public DormantBatchJob(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		this.emailProvider = new EmailProvider.Fake();
	}

	public void execute() {
		// 1. 고객 조회

		// 2. 휴면계정 대상을 추출 및 변환한다.

		// 3. 휴면계정으로 상태를 변경한다.

		// 4. 메일을 보낸다.

	}
}
