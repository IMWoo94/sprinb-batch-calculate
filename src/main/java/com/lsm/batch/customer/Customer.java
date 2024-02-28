package com.lsm.batch.customer;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String email;
	private LocalDateTime createAt;
	private LocalDateTime loginAt;
	@Enumerated(EnumType.STRING)
	private Status status;

	public Customer(String name, String email) {
		this.name = name;
		this.email = email;
		this.createAt = LocalDateTime.now();
		this.loginAt = LocalDateTime.now();
		this.status = Status.NORMAL;
	}

	public void setLoginAt(LocalDateTime loginAt) {
		this.loginAt = loginAt;
	}

	public enum Status {
		NORMAL,
		DORMANT;
	}
}
