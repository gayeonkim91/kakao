package com.kakao.domain.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DistributionRedisService {
	private final RedisTemplate<String, BigDecimal> redisTemplate;

	public boolean exists(String token, String roomId) {
		return redisTemplate.hasKey(getKey(token, roomId));
	}

	public void setAdd(String token, String roomId, BigDecimal... values) {
		redisTemplate.opsForSet().add(getKey(token, roomId), values);
	}

	public BigDecimal getRandOne(String token, String roomId) {
		return redisTemplate.opsForSet().randomMember(getKey(token, roomId));
	}

	public void remove(String token, String roomId, BigDecimal value) {
		redisTemplate.opsForSet().remove(getKey(token, roomId), value);
	}

	public void expire(String token, String roomId, long time, TimeUnit timeUnit) {
		redisTemplate.expire(getKey(token, roomId), time, timeUnit);
	}

	private String getKey(String token, String roomId) {
		String keyFormat = "distribution:token:%s:roomId:%s";
		return String.format(keyFormat, token, roomId);
	}
}
