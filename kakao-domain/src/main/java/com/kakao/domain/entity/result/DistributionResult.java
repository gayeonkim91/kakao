package com.kakao.domain.entity.result;

import com.kakao.domain.entity.request.DistributionRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "distribution_results")
public class DistributionResult {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long distributionResultId;

	@Column
	private Integer receivedUserId;

	@Column
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name="distributionRequestId", referencedColumnName = "distributionRequestId")
	private DistributionRequest distributionRequest;

	@Column
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = Optional.ofNullable(this.createdAt).orElse(now);
	}
}
