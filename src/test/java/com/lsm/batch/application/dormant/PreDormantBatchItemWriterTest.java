package com.lsm.batch.application.dormant;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lsm.batch.EmailProvider;
import com.lsm.batch.customer.Customer;

class PreDormantBatchItemWriterTest {

	private PreDormantBatchItemWriter preDormantBatchItemWriter;

	@Test
	@DisplayName("1주일 뒤에 휴면계정전환 예정자라고 이메일을 전송해야 한다.")
	void test1() {

		// given
		final EmailProvider mockEmailProvider = mock(EmailProvider.class);
		preDormantBatchItemWriter = new PreDormantBatchItemWriter(mockEmailProvider);

		Customer customer = new Customer("lee", "lee@test.com");

		// when
		preDormantBatchItemWriter.write(customer);

		// then
		// 최소 한번이라도 불리게 되었는지
		verify(mockEmailProvider, atLeast(1)).send(any(), any(), any());

	}

}