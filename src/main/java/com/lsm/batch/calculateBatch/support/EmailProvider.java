package com.lsm.batch.calculateBatch.support;

import lombok.extern.slf4j.Slf4j;

public interface EmailProvider {

	void send(String emailAddress, String title, String body);

	@Slf4j
	class FakeEmail implements EmailProvider {
		@Override
		public void send(String emailAddress, String title, String body) {
			log.info("{} email 전송 완료! \n{} \n{}", emailAddress, title, body);
		}
	}
}
