package com.kakao.interfaces.application;

import com.kakao.domain.application.DistributionRedisService;
import com.kakao.domain.application.DistributionRequestDomainService;
import com.kakao.domain.application.vo.DistributionRequestSaveVO;
import com.kakao.interfaces.controller.dto.DistributionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DistributionRequestService {
	private final DistributionRequestDomainService distributionRequestDomainService;
	private final DistributionRedisService distributionRedisService;
	private final Random random = new Random();
	private final int EXPIRE_TIME = 10;

	@Transactional
	public String distribution(DistributionRequestDTO requestDTO, Integer userId, String roomId) {
		String token = DistributionTokenGenerator.generate();
		distributionRequestDomainService.save(
			DistributionRequestSaveVO.builder()
				.requestedUserId(userId)
				.roomId(roomId)
				.token(token)
				.amount(requestDTO.getPrice())
				.memberCount(requestDTO.getMemberCount())
				.build()
		);
		distributionRedisService.setAdd(token, roomId, getRandomAmountList(requestDTO.getPrice(), requestDTO.getMemberCount()));
		distributionRedisService.expire(token, roomId, EXPIRE_TIME, TimeUnit.MINUTES);
		return token;
	}

	private BigDecimal[] getRandomAmountList(BigDecimal totalAmount, int memberCount) {
		BigDecimal[] result = new BigDecimal[memberCount];
		if (totalAmount.intValue() == memberCount) {
			Arrays.fill(result, BigDecimal.valueOf(1));
			return result;
		}
		for (int i = 0; i < memberCount - 1; i++) {
			int bound = totalAmount.intValue() - (memberCount - i -1);
			BigDecimal amount = BigDecimal.valueOf(random.nextInt(bound) + 1);
			result[i] = amount;
			totalAmount = totalAmount.subtract(amount);
		}
		result[memberCount - 1] = totalAmount;
		return result;
	}
}
