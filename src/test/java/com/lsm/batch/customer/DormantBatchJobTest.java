package com.lsm.batch.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DormantBatchJobTest {

	@Test
	@DisplayName("로그인 시간이 일년을 경과한 고객이 3명이고, 일년 이내의 로그인 한 고객이 5명이면 3명의 고객이 휴면 전환 대상 이다.")
	void test1() {

	}

	@Test
	@DisplayName("고객이 10명이 있지만 모두 다 휴면 전환 대상이 아니면 휴면 전환 대상은 0명이다.")
	void test2() {

	}

	@Test
	@DisplayName("고객이 없는 경우도 배치는 정상 동작 해야 한다.")
	void test() {
		// null, size 0 등 예외 적인 상황을 방지
	}
}