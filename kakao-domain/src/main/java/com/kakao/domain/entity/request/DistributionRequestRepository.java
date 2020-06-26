package com.kakao.domain.entity.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRequestRepository extends JpaRepository<DistributionRequest, Long> {
	DistributionRequest findTopByTokenAndRoomId(String token, String roomId);
}
