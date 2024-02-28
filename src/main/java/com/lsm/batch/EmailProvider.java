package com.lsm.batch;

import lombok.extern.slf4j.Slf4j;

// 실제 이메일을 전송 하지 않고 log 만 작성
public interface EmailProvider {

	void send(String emailAddress, String title, String body);

	@Slf4j
	class Fake implements EmailProvider {

		@Override
		public void send(String emailAddress, String title, String body) {
			log.info("{} email 전송 완료! {} :  {}", emailAddress, title, body);
		}
	}
}
