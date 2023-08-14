package com.example.springredisdemo.service;

import com.example.springredisdemo.configuration.RateLimitRulesConfig;
import com.example.springredisdemo.dto.RequestDescriptor;
import com.example.springredisdemo.model.RateLimitRule;
import com.example.springredisdemo.util.RuleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JedisRateLimitService implements RateLimitService {
    private final String REDIS_KEY_PREFIX = "ratelimit:";
    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitRulesConfig rateLimitRulesConfig;
    private final RuleUtil util;

    @Autowired
    public JedisRateLimitService(RateLimitRulesConfig rateLimitRulesConfig,
                                 RedisTemplate<String, String> redisTemplate,
                                 RuleUtil util) {
        this.rateLimitRulesConfig = rateLimitRulesConfig;
        this.redisTemplate = redisTemplate;
        this.util = util;
    }

    @Override
    public boolean allowRequest(List<RequestDescriptor> descriptors) {
        for (RequestDescriptor descriptor : descriptors) {
            String redisKey = REDIS_KEY_PREFIX + generateRedisKey(descriptor);
            List<RateLimitRule> rateLimitRules = rateLimitRulesConfig.getRateLimitRules();
            List<RateLimitRule> matchesRules = util.findAllMatchedRules(rateLimitRules, descriptor);
            if (!matchesRules.isEmpty()) {
                String currentRequestsStr = redisTemplate.opsForValue().get(redisKey);
                Integer currentRequests = currentRequestsStr != null ? Integer.parseInt(currentRequestsStr) - 1 : 1;
                Optional<RateLimitRule> first = matchesRules.stream().findFirst();
                if (first.isPresent()) {
                    final RateLimitRule rateLimitRule = first.get();
                    final TimeUnit timeUnit = TimeUnit.valueOf(rateLimitRule.getTimeInterval().name());
                    if (currentRequestsStr == null && rateLimitRule.getAllowedNumberOfRequests() > 0) {
                        redisTemplate.opsForValue().set(redisKey, "" + (rateLimitRule.getAllowedNumberOfRequests() - 1));
                        redisTemplate.expire(redisKey, 1, timeUnit);
                    } else if (currentRequests < 0 || rateLimitRule.getAllowedNumberOfRequests() <= 0) {
                        return false;
                    } else {
                        redisTemplate.opsForValue().set(redisKey, "" + (Integer.parseInt(redisTemplate.opsForValue().get(redisKey)) - 1), redisTemplate.getExpire(redisKey, timeUnit));
                    }
                }
            }
        }
        return true;
    }

    // Generate a unique key based on the descriptor fields,
    // using accountId, clientIp, and requestType to create a key
    public String generateRedisKey(RequestDescriptor descriptor) {
        String KEY = "";
        KEY += descriptor.getAccountId() != null ? descriptor.getAccountId() + ":" : ":";
        KEY += descriptor.getClientIp() != null ? descriptor.getClientIp() + ":" : ":";
        KEY += descriptor.getRequestType() != null ? descriptor.getRequestType() : "";
        return KEY;
    }
}