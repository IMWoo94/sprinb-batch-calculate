package com.lsm.batch.calculateBatch.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lsm.batch.calculateBatch.domain.SettleGroup;

@Repository
public interface SettleGroupRepository extends JpaRepository<SettleGroup, Long> {

	@Query(
		value = """
			select new SettleGroup(detail.customerId, detail.serviceId, sum(detail.count), sum(detail.fee))
			from SettleDetail detail
			where detail.targetDate between :start and :end
			and detail.customerId = :customerId
			group by detail.customerId, detail.serviceId
			"""
	)
	List<SettleGroup> findGroupByCustomerIdAndServiceId(LocalDate start, LocalDate end, Long customerId);

}
