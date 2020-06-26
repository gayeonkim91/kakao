package com.kakao.interfaces.application;

import com.kakao.domain.application.DistributionRedisService;
import com.kakao.domain.application.DistributionRequestDomainService;
import com.kakao.domain.application.DistributionResultDomainService;
import com.kakao.domain.application.vo.DistributionRequestSaveVO;
import com.kakao.domain.application.vo.DistributionRequestVO;
import com.kakao.domain.application.vo.DistributionResultSaveVO;
import com.kakao.interfaces.common.exception.BadRequestException;
import com.kakao.domain.common.RetrieveFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DistributionReceiveService {
	private final DistributionRedisService distributionRedisService;
	private final DistributionRequestDomainService distributionRequestDomainService;
	private final DistributionResultDomainService distributionResultDomainService;

	@Transactional
	public BigDecimal receive(String token, Integer userId, String roomId) {
		validateExpiredToken(token, roomId);
		DistributionRequestVO distributionRequestVO = distributionRequestDomainService.getDistributionRequest(token, roomId);
		validateRequestVO(distributionRequestVO, userId);
		BigDecimal amount = distributionRedisService.getRandOne(token, roomId);
		saveResult(distributionRequestVO, userId, amount);
		distributionRedisService.remove(token, roomId, amount);
		return amount;
	}

	private void saveResult(DistributionRequestVO distributionRequestVO, Integer userId, BigDecimal amount) {
		DistributionRequestSaveVO requestSaveVO = DistributionRequestSaveVO.builder()
			.distributionRequestId(distributionRequestVO.getDistributionRequestId())
			.requestedUserId(distributionRequestVO.getRequestedUserId())
			.roomId(distributionRequestVO.getRoomId())
			.token(distributionRequestVO.getToken())
			.amount(distributionRequestVO.getAmount())
			.memberCount(distributionRequestVO.getMemberCount())
			.build();
		DistributionResultSaveVO resultSaveVO = DistributionResultSaveVO.builder()
			.receivedUserId(userId)
			.amount(amount)
			.distributionRequest(requestSaveVO)
			.build();

		distributionResultDomainService.save(resultSaveVO);
	}

	private void validateExpiredToken(String token, String roomId) {
		if (!distributionRedisService.exists(token, roomId)) {
			throw new RetrieveFailureException("생성된지 10분이 지나거나 이미 다 받은 뿌리기에서는 받을 수 없습니다.");
		}
	}

	private void validateRequestVO(DistributionRequestVO distributionRequestVO, Integer userId) {
		if (distributionRequestVO.getRequestedUserId().equals(userId)) {
			throw new BadRequestException("뿌린 사람은 받을 수 없습니다.");
		}
		distributionRequestVO.getDistributionResultList().forEach(result -> {
			if (result.getReceivedUserId().equals(userId)) {
				throw new BadRequestException("이미 받은 사람은 받을 수 없습니다.");
			}
		});
	}
}
