package com.kakao.domain.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class DistributionRedisServiceTest extends Specification {
	DistributionRedisService distributionRedisService
	RedisTemplate<String, BigDecimal> redisTemplate = Mock()
	SetOperations<String, BigDecimal> setOperations = Mock()

	void setup() {
		distributionRedisService = new DistributionRedisService(redisTemplate)
		redisTemplate.opsForSet() >> setOperations
	}

	def "Exists"() {
		given:
		redisTemplate.hasKey(_ as String) >> true

		expect:
		distributionRedisService.exists("token", "roomId")
	}

	def "SetAdd"() {
		when:
		distributionRedisService.setAdd("token", "roomId", BigDecimal.TEN)

		then:
		1 * setOperations.add(_ as String, _ as BigDecimal[]) >> null
	}

	def "GetRandOne"() {
		given:
		setOperations.randomMember(_ as String) >> BigDecimal.TEN

		expect:
		distributionRedisService.getRandOne("token", "roomId") == BigDecimal.TEN
	}

	def "Remove"() {
		when:
		distributionRedisService.remove("token", "roomId", BigDecimal.TEN)

		then:
		1 * setOperations.remove(_ as String, _ as Object[]) >> null
	}

	def "Expire"() {
		when:
		distributionRedisService.expire("token", "roomId", 10, TimeUnit.MINUTES)

		then:
		1 * redisTemplate.expire(_ as String, _ as Long, _ as TimeUnit) >> null
	}
}
