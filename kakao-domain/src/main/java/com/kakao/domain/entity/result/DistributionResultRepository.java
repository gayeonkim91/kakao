package com.kakao.domain.entity.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionResultRepository extends JpaRepository<DistributionResult, Long> {
}
