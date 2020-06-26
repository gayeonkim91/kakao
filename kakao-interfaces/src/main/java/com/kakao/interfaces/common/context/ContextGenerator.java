package com.kakao.interfaces.common.context;

import javax.servlet.http.HttpServletRequest;

public class ContextGenerator {
	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	private ContextGenerator() {

	}

	public static ClientContext generateContext(HttpServletRequest request) {
		Integer userId = request.getIntHeader(ContextGenerator.USER_ID_HEADER);
		String roomId = request.getHeader(ContextGenerator.ROOM_ID_HEADER);

		return ClientContext.builder()
			.userId(userId)
			.roomId(roomId)
			.build();
	}
}
