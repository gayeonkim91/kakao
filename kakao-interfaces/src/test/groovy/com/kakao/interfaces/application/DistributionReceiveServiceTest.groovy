package com.kakao.interfaces.application

import com.kakao.domain.application.DistributionRedisService
import com.kakao.domain.application.DistributionRequestDomainService
import com.kakao.domain.application.DistributionResultDomainService
import com.kakao.domain.application.vo.DistributionRequestVO
import com.kakao.domain.application.vo.DistributionResultSaveVO
import com.kakao.domain.application.vo.DistributionResultVO
import com.kakao.domain.common.RetrieveFailureException
import com.kakao.interfaces.common.exception.BadRequestException
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

class DistributionReceiveServiceTest extends Specification {
	DistributionReceiveService distributionReceiveService
	DistributionRedisService distributionRedisService = Mock()
	DistributionRequestDomainService distributionRequestDomainService = Mock()
	DistributionResultDomainService distributionResultDomainService = Mock()

	void setup() {
		distributionReceiveService = new DistributionReceiveService(distributionRedisService,
				distributionRequestDomainService, distributionResultDomainService)
	}

	def "Receive 정상케이스"() {
		given:
		distributionRedisService.exists(_ as String, _ as String) >> true
		distributionRedisService.getRandOne(_ as String, _ as String) >> BigDecimal.valueOf(10)
		distributionRedisService.expire(_ as String, _ as String, _ as Long, _ as TimeUnit) >> null
		distributionRedisService.remove(_ as String, _ as String, _ as BigDecimal) >> null
		def userList = new ArrayList<DistributionResultVO>()
		userList.add(DistributionResultVO.builder().receivedUserId(789).build())
		distributionRequestDomainService.getDistributionRequest(_ as String, _ as String) >>
				DistributionRequestVO.builder()
						.distributionRequestId(1L)
						.requestedUserId(456)
						.distributionResultList(userList)
						.build()
		distributionResultDomainService.save(_ as DistributionResultSaveVO) >> null

		when:
		def result = distributionReceiveService.receive("token", 123, "roomId")

		then:
		result == BigDecimal.valueOf(10)
	}

	@Unroll
	def "Receive 실패케이스"() {
		distributionRedisService.exists(_ as String, _ as String) >> TOKEN_EXISTS
		distributionRedisService.getRandOne(_ as String, _ as String) >> BigDecimal.valueOf(10)
		distributionRedisService.expire(_ as String, _ as String, _ as Long, _ as TimeUnit) >> null
		distributionRedisService.remove(_ as String, _ as String, _ as BigDecimal) >> null
		def userList = new ArrayList<DistributionResultVO>()
		userList.add(DistributionResultVO.builder().receivedUserId(REQUEST_USER).build())
		distributionRequestDomainService.getDistributionRequest(_ as String, _ as String) >>
				DistributionRequestVO.builder()
						.distributionRequestId(1L)
						.requestedUserId(RECEIVED_USER)
						.distributionResultList(userList)
						.build()
		distributionResultDomainService.save(_ as DistributionResultSaveVO) >> null

		when:
		distributionReceiveService.receive("token", 123, "roomId")

		then:
		thrown(THROWN_ERROR)

		where:
		TOKEN_EXISTS | REQUEST_USER | RECEIVED_USER | THROWN_ERROR
		false        | 456          | 789           | RetrieveFailureException.class
		true         | 123          | 789           | BadRequestException.class
		true         | 456          | 123           | BadRequestException.class
	}
}
