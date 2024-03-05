package com.lsm.batch.calculateBatch.generator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.lsm.batch.calculateBatch.domain.ApiOrder;
import com.lsm.batch.calculateBatch.domain.ServicedPolicy;

@Component
public class ApiOrderGeneratorProcessor implements ItemProcessor<Boolean, ApiOrder> {
	// 0 ~ 19 까지 20명의 랜덤 고객 Id 생성
	private final List<Long> customerIds = LongStream.range(0, 20).boxed().toList();
	private final List<ServicedPolicy> servicePolices = Arrays.stream(ServicedPolicy.values()).toList();
	private final ThreadLocalRandom random = ThreadLocalRandom.current();
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	@Override
	public ApiOrder process(Boolean item) throws Exception {

		Long randomCustomId = customerIds.get(random.nextInt(customerIds.size()));
		ServicedPolicy randomServicedPolicy = servicePolices.get(random.nextInt(servicePolices.size()));

		ApiOrder.State randomState = random.nextInt(5) % 5 == 1 ? ApiOrder.State.FAIL : ApiOrder.State.SUCCESS;
		return new ApiOrder(
			UUID.randomUUID().toString(),
			randomCustomId,
			randomServicedPolicy.getUrl(),
			randomState,
			LocalDateTime.now().format(dateTimeFormatter)
		);
	}
}
