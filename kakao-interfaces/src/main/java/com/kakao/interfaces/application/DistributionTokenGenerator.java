package com.kakao.interfaces.application;

import java.util.Random;

public class DistributionTokenGenerator {
	private static final Random r = new Random();
	private static final int TOKEN_SIZE = 3;

	public static String generate() {
		StringBuilder sb = new StringBuilder(TOKEN_SIZE);
		for (int i = 0; i < TOKEN_SIZE; i++) {
			sb.append(generateChar());
		}
		return sb.toString();
	}

	private static char generateChar() {
		switch (r.nextInt(3)) {
			case 0:
				return (char) ('0' + r.nextInt('9' - '0'));
			case 1:
				return (char) ('a' + r.nextInt('z' - 'a'));
			default:
				return (char) ('A' + r.nextInt('Z' - 'A'));
		}
	}
}
