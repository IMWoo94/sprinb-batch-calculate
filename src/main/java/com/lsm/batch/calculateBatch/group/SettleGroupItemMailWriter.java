package com.lsm.batch.calculateBatch.group;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.lsm.batch.calculateBatch.domain.Customer;
import com.lsm.batch.calculateBatch.domain.ServicedPolicy;
import com.lsm.batch.calculateBatch.domain.SettleGroup;
import com.lsm.batch.calculateBatch.domain.repository.CustomerRepository;
import com.lsm.batch.calculateBatch.support.EmailProvider;

@Component
public class SettleGroupItemMailWriter implements ItemWriter<List<SettleGroup>> {

	private final CustomerRepository customerRepository;
	private final EmailProvider emailProvider;

	public SettleGroupItemMailWriter() {
		this.customerRepository = new CustomerRepository.Fake();
		this.emailProvider = new EmailProvider.FakeEmail();
	}

	// 유료 API 총 사용 수, 총 요금
	// 세부사항에 대해서 ( url, 횟수, 요금 )
	@Override
	public void write(Chunk<? extends List<SettleGroup>> chunk) throws Exception {
		for (List<SettleGroup> settleGroups : chunk) {
			if (settleGroups.isEmpty())
				continue;

			final SettleGroup settleGroup = settleGroups.get(0);
			Long customerId = settleGroup.getCustomerId();

			Customer customer = customerRepository.findById(customerId);
			Long totalCount = settleGroups.stream().map(SettleGroup::getTotalCount).reduce(0L, Long::sum);
			Long totalFee = settleGroups.stream().map(SettleGroup::getTotalFee).reduce(0L, Long::sum);
			List<String> detailByService = settleGroups
				.stream()
				.map(it ->
					"\n\"%s\" - 총 사용 : %s 총 비용 : %s".formatted(
						ServicedPolicy.findById(it.getServiceId()).getUrl(),
						it.getTotalCount(),
						it.getTotalFee()
					)
				)
				.toList();

			String body = """
				안녕하세요. %s 고객님. 사용하신 유료 API 과금안내 드립니다.
				총 %s 건을 사용하셨으며, %s원의 비용이 발생했습니다.
				세부내역은 다음과 같습니다. 감사합니다.
				%s
				""".formatted(
				customer.getName(),
				totalCount,
				totalFee,
				detailByService
			);

			emailProvider.send(customer.getEmail(), "유료 API 과금 안내", body);
		}

	}
}
