package com.lsm.batch.calculateBatch.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@Getter
public class SettleGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long customerId;
	private Long serviceId;
	private Long totalCount;
	private Long totalFee;
	private LocalDateTime createAt;

	public SettleGroup(Long customerId, Long serviceId, Long totalCount, Long totalFee) {
		this.customerId = customerId;
		this.serviceId = serviceId;
		this.totalCount = totalCount;
		this.totalFee = totalFee;
		this.createAt = LocalDateTime.now();
	}
}
