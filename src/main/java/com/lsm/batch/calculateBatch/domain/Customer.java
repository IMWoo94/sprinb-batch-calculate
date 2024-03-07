package com.lsm.batch.calculateBatch.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Customer {

	private Long id;

	private String name;

	private String email;

	private LocalDateTime createAt;

	private LocalDateTime updateAt;

	public Customer(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.createAt = LocalDateTime.now();
		this.updateAt = LocalDateTime.now();
	}
}
