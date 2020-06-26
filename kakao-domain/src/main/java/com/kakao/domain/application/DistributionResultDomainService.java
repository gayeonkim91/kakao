package com.kakao.domain.application;

import com.kakao.domain.application.vo.DistributionResultSaveVO;
import com.kakao.domain.entity.result.DistributionResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DistributionResultDomainService {
	private final DistributionResultRepository distributionResultRepository;

	public void save(DistributionResultSaveVO saveVO) {
		distributionResultRepository.save(saveVO.toEntity());
	}
}
