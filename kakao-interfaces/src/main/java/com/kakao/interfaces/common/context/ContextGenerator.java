package com.kakao.interfaces.common.context;

import com.kakao.interfaces.common.exception.BadRequestException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class ContextGenerator {
	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	private ContextGenerator() {

	}

	public static ClientContext generateContext(HttpServletRequest request) {
		int userId = request.getIntHeader(ContextGenerator.USER_ID_HEADER);
		String roomId = request.getHeader(ContextGenerator.ROOM_ID_HEADER);

		if (userId <= 0 || StringUtils.isEmpty(roomId)) {
			throw new BadRequestException("userId나 roomId가 없습니다.");
		}

		return ClientContext.builder()
			.userId(userId)
			.roomId(roomId)
			.build();
	}
}
