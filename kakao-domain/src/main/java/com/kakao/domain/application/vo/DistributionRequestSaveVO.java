package com.kakao.domain.application.vo;

import com.kakao.domain.entity.request.DistributionRequest;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DistributionRequestSaveVO {
	private Long distributionRequestId;
	private Integer requestedUserId;
	private String roomId;
	private String token;
	private BigDecimal amount;
	private Integer memberCount;

	public DistributionRequest toEntity() {
		DistributionRequest entity = new DistributionRequest();
		entity.setDistributionRequestId(this.distributionRequestId);
		entity.setRequestedUserId(this.requestedUserId);
		entity.setRoomId(this.roomId);
		entity.setToken(this.token);
		entity.setAmount(this.amount);
		entity.setMemberCount(this.memberCount);
		return entity;
	}
}
