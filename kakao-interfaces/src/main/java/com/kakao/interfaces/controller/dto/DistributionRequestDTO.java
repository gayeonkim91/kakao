package com.kakao.interfaces.controller.dto;

import com.kakao.interfaces.common.exception.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Getter
@Setter
public class DistributionRequestDTO {
	@Min(value = 1, message = "뿌리기 할 가격은 0보다 커야 합니다.")
	private BigDecimal price;

	@Min(value = 1, message = "1명 이상에게 뿌리기 해야 합니다.")
	private Integer memberCount;

	public void validate() {
		if (price.intValue() < memberCount) {
			throw new BadRequestException("뿌리기 할 가격은 뿌리기 할 인원 보다 크거나 같아야 합니다.");
		}
	}
}
