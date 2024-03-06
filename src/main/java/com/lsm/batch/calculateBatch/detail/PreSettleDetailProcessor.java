package com.lsm.batch.calculateBatch.detail;

import org.springframework.batch.item.ItemProcessor;

import com.lsm.batch.calculateBatch.domain.ApiOrder;
import com.lsm.batch.calculateBatch.domain.ServicedPolicy;

public class PreSettleDetailProcessor implements ItemProcessor<ApiOrder, Key> {
	@Override
	public Key process(ApiOrder item) throws Exception {
		if (item.getState() == ApiOrder.State.FAIL) {
			return null;
		}
		final Long serviceId = ServicedPolicy.findByUrl(item.getUrl()).getId();

		return new Key(
			item.customerId,
			serviceId
		);
	}
}
