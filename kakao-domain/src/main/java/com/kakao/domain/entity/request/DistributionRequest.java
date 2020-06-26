package com.kakao.domain.entity.request;

import com.kakao.domain.entity.result.DistributionResult;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "distribution_requests")
public class DistributionRequest {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long distributionRequestId;
	@Column
	private Integer requestedUserId;
	@Column
	private String roomId;
	@Column
	private String token;
	@Column
	private BigDecimal amount;
	@Column
	private Integer memberCount;
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "distributionRequest")
	private List<DistributionResult> distributionResultList = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = Optional.ofNullable(this.createdAt).orElse(now);
	}
}
