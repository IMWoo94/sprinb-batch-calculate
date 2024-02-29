package com.lsm.batch.application.dormant;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lsm.batch.customer.Customer;

class PreDormantBatchItemProcessorTest {

	private PreDormantBatchItemProcessor preDormantBatchItemProcessor;

	@BeforeEach
	void setup() {
		preDormantBatchItemProcessor = new PreDormantBatchItemProcessor();
	}

	@Test
	@DisplayName("로그인 날짜가 오늘로부터 358일 전이면 customer 를 반환해야 한다.")
	void test1() {

		// given
		Customer customer = new Customer("lee", "lee@test.com");

		customer.setLoginAt(LocalDateTime.now().minusDays(365).plusDays(7));

		// when
		Customer result = preDormantBatchItemProcessor.process(customer);

		// then
		Assertions.assertThat(result).isEqualTo(customer);
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	@DisplayName("로그인 날짜가 오늘로부터 358일 전이 아니면 null을 반환해야 한다.")
	void test2() {

		// given
		Customer customer = new Customer("lee", "lee@test.com");

		customer.setLoginAt(LocalDateTime.now());

		// when
		Customer result = preDormantBatchItemProcessor.process(customer);

		// then
		Assertions.assertThat(result).isNull();
	}

}