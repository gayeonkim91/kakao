package com.kakao.domain.application

import com.kakao.domain.application.vo.DistributionRequestSaveVO
import com.kakao.domain.common.RetrieveFailureException
import com.kakao.domain.entity.request.DistributionRequest
import com.kakao.domain.entity.request.DistributionRequestRepository
import spock.lang.Specification

class DistributionRequestDomainServiceTest extends Specification {
	DistributionRequestDomainService distributionRequestDomainService
	DistributionRequestRepository distributionRequestRepository = Mock()

	void setup() {
		distributionRequestDomainService = new DistributionRequestDomainService(distributionRequestRepository)
	}

	def "GetDistributionRequest 정상"() {
		given:
		def distributionRequest = new DistributionRequest()
		distributionRequest.setDistributionRequestId(123)
		distributionRequest.setDistributionResultList(new ArrayList<>())

		when:
		def result = distributionRequestDomainService.getDistributionRequest("token", "roomId")

		then:
		1 * distributionRequestRepository.findTopByTokenAndRoomId(_ as String, _ as String) >> distributionRequest
		result.distributionRequestId == 123
	}

	def "GetDistributionRequest 실패"() {
		when:
		distributionRequestDomainService.getDistributionRequest("token", "roomId")

		then:
		1 * distributionRequestRepository.findTopByTokenAndRoomId(_ as String, _ as String) >> null
		thrown(RetrieveFailureException.class)
	}

	def "Save"() {
		given:
		def request = DistributionRequestSaveVO.builder().build()

		when:
		distributionRequestDomainService.save(request)

		then:
		1 * distributionRequestRepository.save(_ as DistributionRequest) >> null
	}
}
