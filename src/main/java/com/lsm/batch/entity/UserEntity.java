package com.lsm.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	private String name;
	private int age;
	@Column(length = 50)
	private String region;
	@Column(length = 50)
	private String telephone;

	public UserEntity(String name, int age, String region, String telephone) {
		this.name = name;
		this.age = age;
		this.region = region;
		this.telephone = telephone;
	}
}
