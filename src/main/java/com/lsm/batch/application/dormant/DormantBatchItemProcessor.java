package com.lsm.batch.application.dormant;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemProcessor;

@Component
public class DormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer customer) {
		// 2. 휴면계정 대상을 추출 및 변환한다.
		// 로그인 날짜 	/		365일 전			/ 		오늘
		final boolean isDormantTarget = LocalDate.now()
			.minusDays(365)
			.isAfter(customer.getLoginAt().toLocalDate());

		if (isDormantTarget) {
			customer.setStatus(Customer.Status.DORMANT);
			return customer;
		} else {
			return null;
		}
	}
}
