package com.kakao.domain.application;

import com.kakao.domain.application.vo.DistributionRequestSaveVO;
import com.kakao.domain.application.vo.DistributionRequestVO;
import com.kakao.domain.common.RetrieveFailureException;
import com.kakao.domain.entity.request.DistributionRequest;
import com.kakao.domain.entity.request.DistributionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DistributionRequestDomainService {
	private final DistributionRequestRepository distributionRequestRepository;

	public DistributionRequestVO getDistributionRequest(String token, String roomId) {
		DistributionRequest request = distributionRequestRepository.findTopByTokenAndRoomId(token, roomId);
		if (Objects.isNull(request)) {
			throw new RetrieveFailureException("요청에 맞는 뿌리기가 없습니다.");
		}
		return DistributionRequestVO.of(request);
	}

	public void save(DistributionRequestSaveVO saveVO) {
		distributionRequestRepository.save(saveVO.toEntity());
	}
}
