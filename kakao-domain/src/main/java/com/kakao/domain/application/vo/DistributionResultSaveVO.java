package com.kakao.domain.application.vo;

import com.kakao.domain.entity.result.DistributionResult;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class DistributionResultSaveVO {
	private Long distributionResultId;
	private Integer receivedUserId;
	private BigDecimal amount;
	private DistributionRequestSaveVO distributionRequest;

	public DistributionResult toEntity() {
		DistributionResult entity = new DistributionResult();
		entity.setDistributionResultId(this.distributionResultId);
		entity.setReceivedUserId(this.receivedUserId);
		entity.setAmount(this.amount);
		entity.setDistributionRequest(this.distributionRequest.toEntity());
		return entity;
	}
}
