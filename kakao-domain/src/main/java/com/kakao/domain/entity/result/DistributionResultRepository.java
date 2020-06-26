package com.kakao.domain.entity.result;

import com.kakao.domain.entity.request.DistributionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionResultRepository extends JpaRepository<DistributionResult, Long> {
	List<DistributionResult> findAllByDistributionRequest(DistributionRequest distributionRequest);
}
