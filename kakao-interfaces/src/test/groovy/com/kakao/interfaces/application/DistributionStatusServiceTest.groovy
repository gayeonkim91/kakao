package com.kakao.interfaces.application

import com.kakao.domain.application.DistributionRequestDomainService
import com.kakao.domain.application.vo.DistributionRequestVO
import com.kakao.domain.application.vo.DistributionResultVO
import com.kakao.domain.common.RetrieveFailureException
import com.kakao.interfaces.common.exception.BadRequestException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class DistributionStatusServiceTest extends Specification {
	DistributionStatusService distributionStatusService
	DistributionRequestDomainService distributionRequestDomainService = Mock()

	void setup() {
		distributionStatusService = new DistributionStatusService(distributionRequestDomainService)
	}

	def "GetStatus 정상"() {
		given:
		def createdAt = LocalDateTime.now()
		List<DistributionResultVO> list = new ArrayList<DistributionResultVO>()
		list.add(DistributionResultVO.builder()
				.receivedUserId(456)
				.amount(BigDecimal.valueOf(20))
				.build())
		list.add(DistributionResultVO.builder()
				.receivedUserId(789)
				.amount(BigDecimal.valueOf(30))
				.build())
		distributionRequestDomainService.getDistributionRequest(_ as String, _ as String) >>
				DistributionRequestVO.builder()
						.createdAt(createdAt)
						.requestedUserId(123)
						.amount(BigDecimal.valueOf(100))
						.distributionResultList(list)
						.build()

		when:
		def result = distributionStatusService.getStatus("token", 123, "roomId")

		then:
		result.requestedAmount == BigDecimal.valueOf(100)
		result.totalReceivedAmount == BigDecimal.valueOf(50)
		result.distributionInfoList.size() == 2
	}

	@Unroll
	def "GetStatus 실패"() {
		given:
		distributionRequestDomainService.getDistributionRequest(_ as String, _ as String) >>
				DistributionRequestVO.builder()
						.requestedUserId(USER_ID)
						.createdAt(CREATED_AT)
						.build()

		when:
		distributionStatusService.getStatus("token", 123, "roomId")

		then:
		thrown(EXEPTION)

		where:
		USER_ID | CREATED_AT                            | EXEPTION
		456     | LocalDateTime.now()                   | BadRequestException.class
		123     | LocalDateTime.of(2019, 12, 31, 1, 10) | RetrieveFailureException.class
	}
}
