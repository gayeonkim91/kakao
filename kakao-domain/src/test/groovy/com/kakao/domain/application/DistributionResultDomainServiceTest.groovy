package com.kakao.domain.application

import com.kakao.domain.application.vo.DistributionRequestSaveVO
import com.kakao.domain.application.vo.DistributionResultSaveVO
import com.kakao.domain.entity.result.DistributionResult
import com.kakao.domain.entity.result.DistributionResultRepository
import spock.lang.Specification

class DistributionResultDomainServiceTest extends Specification {
	DistributionResultDomainService distributionResultDomainService
	DistributionResultRepository distributionResultRepository = Mock()

	void setup() {
		distributionResultDomainService = new DistributionResultDomainService(distributionResultRepository)
	}

	def "Save"() {
		given:
		def resultVO = DistributionResultSaveVO.builder()
				.distributionResultId(123)
				.distributionRequest(DistributionRequestSaveVO.builder().build())
				.build()

		when:
		distributionResultDomainService.save(resultVO)

		then:
		1 * distributionResultRepository.save(_ as DistributionResult) >> null
	}
}
