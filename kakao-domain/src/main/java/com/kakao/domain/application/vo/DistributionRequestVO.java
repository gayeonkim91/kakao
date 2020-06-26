package com.kakao.domain.application.vo;

import com.kakao.domain.entity.request.DistributionRequest;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class DistributionRequestVO {
	private Long distributionRequestId;
	private Integer requestedUserId;
	private String roomId;
	private String token;
	private BigDecimal amount;
	private Integer memberCount;
	private List<DistributionResultVO> distributionResultList;
	private LocalDateTime createdAt;

	public static DistributionRequestVO of(DistributionRequest entity) {
		return DistributionRequestVO.builder()
			.distributionRequestId(entity.getDistributionRequestId())
			.requestedUserId(entity.getRequestedUserId())
			.roomId(entity.getRoomId())
			.token(entity.getToken())
			.amount(entity.getAmount())
			.memberCount(entity.getMemberCount())
			.distributionResultList(
				entity.getDistributionResultList().stream()
					.map(DistributionResultVO::of).collect(Collectors.toList())
			)
			.createdAt(entity.getCreatedAt())
			.build();
	}
}
