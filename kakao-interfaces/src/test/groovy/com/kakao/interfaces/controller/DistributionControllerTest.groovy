package com.kakao.interfaces.controller

import com.kakao.configuration.HttpRequestHeaderInterceptor
import com.kakao.domain.common.RetrieveFailureException
import com.kakao.interfaces.application.DistributionReceiveService
import com.kakao.interfaces.application.DistributionRequestService
import com.kakao.interfaces.application.DistributionStatusService
import com.kakao.interfaces.common.exception.BadRequestException
import com.kakao.interfaces.controller.dto.DistributionInfoDTO
import com.kakao.interfaces.controller.dto.DistributionRequestDTO
import com.kakao.interfaces.controller.dto.DistributionStatusDTO
import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

class DistributionControllerTest extends Specification {
	DistributionController controller
	MockMvc controllerMock

	DistributionRequestService distributionRequestService = Mock()
	DistributionReceiveService distributionReceiveService = Mock()
	DistributionStatusService distributionStatusService = Mock()

	JsonSlurper jsonSlurper

	void setup() {
		controller = new DistributionController(distributionRequestService, distributionReceiveService, distributionStatusService)
		KakaoApiControllerAdvice controllerAdvice = new KakaoApiControllerAdvice()
		HttpRequestHeaderInterceptor headerInterceptor = new HttpRequestHeaderInterceptor()
		controllerMock = MockMvcBuilders.standaloneSetup(controller).addInterceptors(headerInterceptor).setControllerAdvice(controllerAdvice).build()
		jsonSlurper = new JsonSlurper()
	}

	def "Distribute"() {
		given:
		distributionRequestService.distribution(_ as DistributionRequestDTO, _ as Integer, _ as String) >> "token"

		when:
		def response = controllerMock.perform(
				MockMvcRequestBuilders.post("/distribute")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
						.content("{\"price\":1000, \"memberCount\":3}")
						.contentType(MediaType.APPLICATION_JSON)
		)

		then:
		response.andExpect(MockMvcResultMatchers.status().isOk())
		def result = jsonSlurper.parseText(response.andReturn().getResponse().getContentAsString())
		result.success
		result.message == null
		result.content == "token"
	}

	@Unroll
	def "Distribute 실패"() {
		given:
		distributionRequestService.distribution(_ as DistributionRequestDTO, _ as Integer, _ as String) >> {
			throw new RetrieveFailureException("failure")
		}

		when:
		def actions = controllerMock.perform(
				MockMvcRequestBuilders.post("/distribute")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
						.content("{\"price\":" + PRICE + ", \"memberCount\":" + MEMBER_COUNT + "}")
						.contentType(MediaType.APPLICATION_JSON)
		)

		then:
		actions.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		def response = actions.andReturn().getResponse()
		response.setCharacterEncoding("UTF-8")
		def result = jsonSlurper.parseText(response.getContentAsString())
		!result.success
		result.message == MESSAGE
		result.content == null

		where:
		PRICE | MEMBER_COUNT | MESSAGE
		0     | 3            | "뿌리기 할 가격은 0보다 커야 합니다."
		1000  | 0            | "1명 이상에게 뿌리기 해야 합니다."
		2     | 3            | "뿌리기 할 가격은 뿌리기 할 인원 보다 크거나 같아야 합니다."
		1000  | 3            | "failure"
	}

	def "Receive"() {
		given:
		distributionReceiveService.receive(_ as String, _ as Integer, _ as String) >> BigDecimal.TEN

		when:
		def response = controllerMock.perform(
				MockMvcRequestBuilders.post("/receive/token")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
		)

		then:
		response.andExpect(MockMvcResultMatchers.status().isOk())
		def result = jsonSlurper.parseText(response.andReturn().getResponse().getContentAsString())
		result.success
		result.message == null
		result.content == 10
	}

	def "Receive 실패"() {
		given:
		distributionReceiveService.receive(_ as String, _ as Integer, _ as String) >> {
			throw new BadRequestException("failure")
		}

		when:
		def actions = controllerMock.perform(
				MockMvcRequestBuilders.post("/receive/{token}", "token")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
		)

		then:
		actions.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		def response = actions.andReturn().getResponse()
		response.setCharacterEncoding("UTF-8")
		def result = jsonSlurper.parseText(response.getContentAsString())
		!result.success
		result.message == "failure"
		result.content == null
	}

	def "GetStatus"() {
		given:
		def distributionInfoList = new ArrayList<DistributionInfoDTO>();
		distributionInfoList.add(new DistributionInfoDTO(receivedAmount: BigDecimal.valueOf(500), userId: 123))
		def statusDTO = new DistributionStatusDTO(
				requestedDateTime: "2020-06-27 10:00:00",
				requestedAmount: BigDecimal.valueOf(1000),
				totalReceivedAmount: BigDecimal.valueOf(500),
				distributionInfoList: distributionInfoList
		)
		distributionStatusService.getStatus(_ as String, _ as Integer, _ as String) >> statusDTO

		when:
		def response = controllerMock.perform(
				MockMvcRequestBuilders.get("/status/{token}", "token")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
		)

		then:
		response.andExpect(MockMvcResultMatchers.status().isOk())
		def result = jsonSlurper.parseText(response.andReturn().getResponse().getContentAsString())
		result.success
		result.message == null
		result.content.requestedDateTime == "2020-06-27 10:00:00"
		result.content.requestedAmount == 1000
		result.content.totalReceivedAmount == 500
		result.content.distributionInfoList.size() == 1
		result.content.distributionInfoList[0].receivedAmount == 500
		result.content.distributionInfoList[0].userId == 123
	}

	def "GetStatus 실패"() {
		given:
		distributionStatusService.getStatus(_ as String, _ as Integer, _ as String) >> {
			throw new BadRequestException("failure")
		}

		when:
		def actions = controllerMock.perform(
				MockMvcRequestBuilders.get("/status/{token}", "token")
						.header("X-ROOM-ID", "abc")
						.header("X-USER-ID", 123)
		)

		then:
		actions.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		def response = actions.andReturn().getResponse()
		response.setCharacterEncoding("UTF-8")
		def result = jsonSlurper.parseText(response.getContentAsString())
		!result.success
		result.message == "failure"
		result.content == null
	}

	def "Header에 값이 제대로 들어가 있지 않을 때"() {
		distributionReceiveService.receive(_ as String, _ as Integer, _ as String) >> BigDecimal.TEN

		when:
		def actions = controllerMock.perform(
				MockMvcRequestBuilders.post("/receive/token")
		)

		then:
		actions.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		def response = actions.andReturn().getResponse()
		response.setCharacterEncoding("UTF-8")
		def result = jsonSlurper.parseText(response.getContentAsString())
		!result.success
		result.message == "userId나 roomId가 없습니다."
		result.content == null
	}
}
