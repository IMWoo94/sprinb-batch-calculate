package com.lsm.batch;

import java.time.LocalDateTime;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.customer.CustomerRepository;
import com.lsm.batch.dormantbatch.BatchStatus;
import com.lsm.batch.dormantbatch.Job;
import com.lsm.batch.dormantbatch.JobExecution;
import com.lsm.batch.dormantbatch.TaskletJob;

@SpringBootTest
class DormantBatchJobTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private Job dormantBatchJob;

	@BeforeEach
	public void setup() {
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName("로그인 시간이 일년을 경과한 고객이 3명이고, 일년 이내의 로그인 한 고객이 5명이면 3명의 고객이 휴면 전환 대상 이다.")
	void test1() {
		// given
		saveCustomer(366); // 일년 경과 고객
		saveCustomer(366); // 일년 경과 고객
		saveCustomer(366); // 일년 경과 고객

		saveCustomer(364);
		saveCustomer(364);
		saveCustomer(364);
		saveCustomer(364);
		saveCustomer(364);

		// when
		// 배치 동작 [ 휴면 계정 전환 ]
		JobExecution result = dormantBatchJob.execute();

		// then
		final long dormantCount = customerRepository.findAll()
			.stream()
			.filter(it -> it.getStatus() == Customer.Status.DORMANT)
			.count();

		Assertions.assertThat(dormantCount).isEqualTo(3);
		Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@Test
	@DisplayName("고객이 10명이 있지만 모두 다 휴면 전환 대상 이면 휴면 전환 대상은 10명이다.")
	void test2() {
		// given
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);
		saveCustomer(400);

		// when
		// 배치 동작 [ 휴면 계정 전환 ]
		JobExecution result = dormantBatchJob.execute();

		// then
		final long dormantCount = customerRepository.findAll()
			.stream()
			.filter(it -> it.getStatus() == Customer.Status.DORMANT)
			.count();

		Assertions.assertThat(dormantCount).isEqualTo(10);
		Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@Test
	@DisplayName("고객이 없는 경우도 배치는 정상 동작 해야 한다.")
	void test3() {
		// null, size 0 등 예외 적인 상황을 방지

		// given

		// when
		// 배치 동작 [ 휴면 계정 전환 ]
		JobExecution result = dormantBatchJob.execute();

		// then
		final long dormantCount = customerRepository.findAll()
			.stream()
			.filter(it -> it.getStatus() == Customer.Status.DORMANT)
			.count();

		Assertions.assertThat(dormantCount).isEqualTo(0);
		Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@Test
	@DisplayName("배치가 실패하면 BatchStatus 는 FAILED 를 반환해야 한다.")
	void test4() {

		// given
		final Job dormantBatchJob = new TaskletJob(null);

		// when
		final JobExecution result = dormantBatchJob.execute();

		// then
		Assertions.assertThat(result.getStatus()).isEqualTo(BatchStatus.FAILED);
	}

	private void saveCustomer(long loginMinusDays) {
		final String uuid = UUID.randomUUID().toString();
		final Customer user = new Customer(uuid, uuid + "@test.com");
		user.setLoginAt(LocalDateTime.now().minusDays(loginMinusDays));
		customerRepository.save(user);
	}
}