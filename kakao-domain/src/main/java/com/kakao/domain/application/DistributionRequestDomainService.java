package com.kakao.domain.application;

import com.kakao.domain.application.vo.DistributionRequestSaveVO;
import com.kakao.domain.application.vo.DistributionRequestVO;
import com.kakao.domain.entity.request.DistributionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DistributionRequestDomainService {
	private final DistributionRequestRepository distributionRequestRepository;

	public DistributionRequestVO getDistributionRequest(String token, String roomId) {
		return DistributionRequestVO.of(distributionRequestRepository.findDistinctByTokenAndRoomId(token, roomId));
	}

	public void save(DistributionRequestSaveVO saveVO) {
		distributionRequestRepository.save(saveVO.toEntity());
	}
}
