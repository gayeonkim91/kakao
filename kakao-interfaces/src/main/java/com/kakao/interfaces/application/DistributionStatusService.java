package com.kakao.interfaces.application;

import com.kakao.domain.application.DistributionRequestDomainService;
import com.kakao.domain.application.vo.DistributionRequestVO;
import com.kakao.domain.application.vo.DistributionResultVO;
import com.kakao.interfaces.controller.dto.DistributionInfoDTO;
import com.kakao.interfaces.controller.dto.DistributionStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributionStatusService {
	private final DistributionRequestDomainService distributionRequestDomainService;

	public DistributionStatusDTO getStatus(String token, Integer userId, String roomId) {
		DistributionRequestVO requestVO = distributionRequestDomainService.getDistributionRequest(token, roomId);
		validate(requestVO, userId);
		return generateStatusDTO(requestVO);
	}

	private DistributionStatusDTO generateStatusDTO(DistributionRequestVO requestVO) {
		DistributionStatusDTO distributionStatusDTO = new DistributionStatusDTO();
		distributionStatusDTO.setRequestedDateTime(requestVO.getCreatedAt());
		distributionStatusDTO.setRequestedAmount(requestVO.getAmount());
		List<DistributionInfoDTO> infoList = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (DistributionResultVO resultVO : requestVO.getDistributionResultList()) {
			DistributionInfoDTO info = new DistributionInfoDTO();
			info.setUserId(resultVO.getReceivedUserId());
			info.setReceivedAmount(resultVO.getAmount());
			totalAmount = totalAmount.add(resultVO.getAmount());
			infoList.add(info);
		}
		distributionStatusDTO.setTotalReceivedAmount(totalAmount);
		distributionStatusDTO.setDistributionInfoList(infoList);
		return distributionStatusDTO;
	}

	private void validate(DistributionRequestVO requestVO, Integer userId) {
		if (!requestVO.getRequestedUserId().equals(userId)) {
			throw new IllegalArgumentException();
		}
		if (!requestVO.getCreatedAt().plusDays(7).isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException();
		}
	}
}
