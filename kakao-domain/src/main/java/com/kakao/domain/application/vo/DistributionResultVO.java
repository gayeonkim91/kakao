package com.kakao.domain.application.vo;

import com.kakao.domain.entity.result.DistributionResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class DistributionResultVO {
	private Integer receivedUserId;
	private BigDecimal amount;

	public static DistributionResultVO of(DistributionResult entity) {
		return DistributionResultVO.builder()
			.receivedUserId(entity.getReceivedUserId())
			.amount(entity.getAmount())
			.build();
	}
}
