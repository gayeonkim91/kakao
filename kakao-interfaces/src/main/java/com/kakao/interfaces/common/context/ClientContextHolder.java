package com.kakao.interfaces.common.context;

public final class ClientContextHolder {
	private static final ThreadLocal<ClientContext> holder = new ThreadLocal<>();

	public static void set(ClientContext context) {
		holder.set(context);
	}

	public static ClientContext get() {
		ClientContext clientContext = holder.get();
		if (clientContext == null) {
			throw new RuntimeException("context 생성 실패");
		}
		return clientContext;
	}
}
