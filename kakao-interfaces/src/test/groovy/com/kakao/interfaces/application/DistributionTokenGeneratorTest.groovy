package com.kakao.interfaces.application

import spock.lang.Specification

class DistributionTokenGeneratorTest extends Specification {
	def "Generate"() {
		given:
		def regex = /[0-9a-zA-Z]{3}/

		when:
		def result = DistributionTokenGenerator.generate()

		then:
		result ==~ regex
	}
}
