package com.lsm.batch.application;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.lsm.batch.EmailProvider;
import com.lsm.batch.customer.Customer;
import com.lsm.batch.customer.CustomerRepository;
import com.lsm.batch.dormantbatch.Tasklet;

@Component
public class DormantBatchTasklet implements Tasklet {

	private final CustomerRepository customerRepository;
	private final EmailProvider emailProvider;

	public DormantBatchTasklet(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		this.emailProvider = new EmailProvider.Fake();
	}

	@Override
	public void execute() {
		// 비즈니스 로직
		int pageNo = 0;
		while (true) {
			// 1. 고객 조회
			// 한명의 고객씩 가져오기
			final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
			final Page<Customer> page = customerRepository.findAll(pageRequest);

			final Customer customer;
			if (page.isEmpty()) {
				break;
			} else {
				pageNo++;
				customer = page.getContent().get(0);
			}

			// 2. 휴면계정 대상을 추출 및 변환한다.
			// 로그인 날짜 	/		365일 전			/ 		오늘
			final boolean isDormantTarget = LocalDate.now()
				.minusDays(365)
				.isAfter(customer.getLoginAt().toLocalDate());

			if (isDormantTarget) {
				customer.setStatus(Customer.Status.DORMANT);
			} else {
				continue;
			}

			// 3. 휴면계정으로 상태를 변경한다.
			customerRepository.save(customer);

			// 4. 메일을 보낸다.
			emailProvider.send(customer.getEmail(), "휴면전환 안내메일입니다.", "휴면 전환");
		}
	}
}
