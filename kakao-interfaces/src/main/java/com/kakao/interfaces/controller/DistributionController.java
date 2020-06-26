package com.kakao.interfaces.controller;

import com.kakao.interfaces.application.DistributionReceiveService;
import com.kakao.interfaces.application.DistributionRequestService;
import com.kakao.interfaces.application.DistributionStatusService;
import com.kakao.interfaces.common.ResponseDTO;
import com.kakao.interfaces.common.context.ClientContext;
import com.kakao.interfaces.common.context.ClientContextHolder;
import com.kakao.interfaces.controller.dto.DistributionRequestDTO;
import com.kakao.interfaces.controller.dto.DistributionStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class DistributionController {
	private final DistributionRequestService distributionRequestService;
	private final DistributionReceiveService distributionReceiveService;
	private final DistributionStatusService distributionStatusService;

	@PostMapping(value = "/distribute")
	public ResponseDTO<String> distribute(@RequestBody @Valid DistributionRequestDTO requestDTO) {
		requestDTO.validate();
		ClientContext context = ClientContextHolder.get();
		return ResponseDTO.ofSuccess(
			distributionRequestService.distribution(requestDTO, context.getUserId(), context.getRoomId()));
	}

	@PostMapping(value = "/receive/{token}")
	public ResponseDTO<BigDecimal> receive(@PathVariable String token) {
		ClientContext context = ClientContextHolder.get();
		return ResponseDTO.ofSuccess(distributionReceiveService.receive(token, context.getUserId(), context.getRoomId()));
	}

	@GetMapping("/status/{token}")
	public ResponseDTO<DistributionStatusDTO> getStatus(@PathVariable String token) {
		ClientContext context = ClientContextHolder.get();
		return ResponseDTO.ofSuccess(distributionStatusService.getStatus(token, context.getUserId(), context.getRoomId()));
	}
}
