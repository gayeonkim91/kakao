package com.kakao.interfaces.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DistributionRequestDTO {
	private BigDecimal price;
	private Integer memberCount;
}
