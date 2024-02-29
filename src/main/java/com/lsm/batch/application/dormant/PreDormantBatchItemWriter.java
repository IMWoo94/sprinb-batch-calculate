package com.lsm.batch.application.dormant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lsm.batch.EmailProvider;
import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemWriter;

@Component
public class PreDormantBatchItemWriter implements ItemWriter<Customer> {

	private final EmailProvider emailProvider;

	@Autowired
	public PreDormantBatchItemWriter() {
		this.emailProvider = new EmailProvider.Fake();
	}

	public PreDormantBatchItemWriter(EmailProvider emailProvider) {
		this.emailProvider = emailProvider;
	}

	@Override
	public void write(Customer customer) {
		emailProvider.send(
			customer.getEmail(),
			"휴면 계정 전환 안내 고지 메일",
			"휴면 계정으로 사용되기를 원치 않으신다면 1주일 내에 로그인을 해주세요."
		);
	}
}
