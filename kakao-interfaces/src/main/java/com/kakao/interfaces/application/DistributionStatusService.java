package com.kakao.interfaces.application;

import com.kakao.domain.application.DistributionRequestDomainService;
import com.kakao.domain.application.vo.DistributionRequestVO;
import com.kakao.domain.application.vo.DistributionResultVO;
import com.kakao.interfaces.common.exception.BadRequestException;
import com.kakao.domain.common.RetrieveFailureException;
import com.kakao.interfaces.controller.dto.DistributionInfoDTO;
import com.kakao.interfaces.controller.dto.DistributionStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		distributionStatusDTO.setRequestedDateTime(requestVO.getCreatedAt().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
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
			throw new BadRequestException("뿌리기를 생성한 사람만 조회 가능합니다.");
		}
		if (!requestVO.getCreatedAt().plusMinutes(7).isAfter(LocalDateTime.now())) {
			throw new RetrieveFailureException("뿌리기한지 7일이 지난 뿌리기는 조회할 수 없습니다.");
		}
	}
}
