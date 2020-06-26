package com.kakao.interfaces.application

import com.kakao.domain.application.DistributionRedisService
import com.kakao.domain.application.DistributionRequestDomainService
import com.kakao.domain.application.vo.DistributionRequestSaveVO
import com.kakao.interfaces.controller.dto.DistributionRequestDTO
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class DistributionRequestServiceTest extends Specification {
	DistributionRequestService distributionRequestService
	DistributionRequestDomainService distributionRequestDomainService = Mock()
	DistributionRedisService distributionRedisService = Mock()

	void setup() {
		distributionRequestService = new DistributionRequestService(distributionRequestDomainService, distributionRedisService)
	}

	def "Distribution"() {
		given:
		DistributionRequestDTO requestDTO = new DistributionRequestDTO()
		requestDTO.setPrice(BigDecimal.valueOf(3))
		requestDTO.setMemberCount(3)

		when:
		distributionRequestService.distribution(requestDTO, 123, "abc")

		then:
		1 * distributionRequestDomainService.save(_ as DistributionRequestSaveVO) >> null
		1 * distributionRedisService.setAdd(_ as String, _ as String, _ as BigDecimal[]) >> null
		1 * distributionRedisService.expire(_ as String, _ as String, _ as Long, _ as TimeUnit) >> null
	}

	def "getRandomAmountList"() {
		when:
		def result = distributionRequestService.getRandomAmountList(BigDecimal.TEN, 9)

		then:
		for (int value : result) {
			value >= 1
		}
		result.sum() == BigDecimal.TEN
	}
}
