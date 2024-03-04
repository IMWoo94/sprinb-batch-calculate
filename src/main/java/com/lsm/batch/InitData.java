package com.lsm.batch;

import org.springframework.stereotype.Component;

import com.lsm.batch.entity.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitData {

	private final UserRepository userEntityRepository;

	@PostConstruct
	public void init() {
		userEntityRepository.save(new User("민수", 20, "서울", "010-1111-1111"));
		userEntityRepository.save(new User("민주", 10, "부산", "010-2222-2222"));
		userEntityRepository.save(new User("민우", 20, "경기", "010-3333-3333"));
		userEntityRepository.save(new User("민욱", 30, "대구", "010-4444-4444"));
		userEntityRepository.save(new User("민구", 50, "강릉", "010-5555-5555"));
	}
}
