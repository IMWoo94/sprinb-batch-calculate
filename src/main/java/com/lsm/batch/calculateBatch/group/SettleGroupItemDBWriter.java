package com.lsm.batch.calculateBatch.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.lsm.batch.calculateBatch.domain.SettleGroup;
import com.lsm.batch.calculateBatch.domain.repository.SettleGroupRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettleGroupItemDBWriter implements ItemWriter<List<SettleGroup>> {

	private final SettleGroupRepository settleGroupRepository;

	@Override
	public void write(Chunk<? extends List<SettleGroup>> chunk) throws Exception {
		final List<SettleGroup> settleGroups = new ArrayList<>();

		chunk.forEach(settleGroups::addAll);

		settleGroupRepository.saveAll(settleGroups);

	}
}
