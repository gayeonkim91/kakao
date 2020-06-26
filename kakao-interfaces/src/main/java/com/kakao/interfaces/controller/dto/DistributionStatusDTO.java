package com.kakao.interfaces.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DistributionStatusDTO {
	private String requestedDateTime;
	private BigDecimal requestedAmount;
	private BigDecimal totalReceivedAmount;
	private List<DistributionInfoDTO> distributionInfoList;
}
