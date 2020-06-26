package com.kakao.interfaces.common.context;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientContext {
	private String roomId;
	private Integer userId;
}
