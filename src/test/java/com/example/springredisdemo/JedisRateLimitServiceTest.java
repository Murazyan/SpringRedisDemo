package com.example.springredisdemo;

import com.example.springredisdemo.configuration.RateLimitRulesConfig;
import com.example.springredisdemo.dto.RequestDescriptor;
import com.example.springredisdemo.model.RateLimitRule;
import com.example.springredisdemo.model.TimeInterval;
import com.example.springredisdemo.service.JedisRateLimitService;
import com.example.springredisdemo.util.RuleUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JedisRateLimitServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private RateLimitRulesConfig rateLimitRulesConfig;

    @Mock
    private RuleUtil util;

    private JedisRateLimitService rateLimitService;

    @Before
    public void setup() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        rateLimitService = new JedisRateLimitService(rateLimitRulesConfig, redisTemplate, util);
    }

    @Test
    public void testAllowRequest() {
        // Prepare test data
        RequestDescriptor descriptor = new RequestDescriptor("accountId1", "clientIp1", "requestType1");
        List<RequestDescriptor> descriptors = Arrays.asList(descriptor);

        RateLimitRule rule = new RateLimitRule();
        rule.setAccountId("accountId1");
        rule.setAllowedNumberOfRequests(2);
        rule.setTimeInterval(TimeInterval.MINUTES);

        when(rateLimitRulesConfig.getRateLimitRules()).thenReturn(Arrays.asList(rule));
        when( util.findAllMatchedRules(anyList(), any())).thenReturn(Arrays.asList(rule));

        boolean result = rateLimitService.allowRequest(descriptors);

        assertTrue(result);

    }

    @Test
    public void testAllowRequest_ExceedLimit() {
        List<RequestDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new RequestDescriptor("1", "127.0.0.1", "login"));

        RateLimitRule rule = new RateLimitRule();
        rule.setAccountId("1");
        rule.setAllowedNumberOfRequests(0);
        rule.setTimeInterval(TimeInterval.MINUTES);
        when(rateLimitRulesConfig.getRateLimitRules()).thenReturn(Collections.singletonList(rule));
        when( util.findAllMatchedRules(anyList(), any())).thenReturn(Arrays.asList(rule));

        boolean result = rateLimitService.allowRequest(descriptors);

        assertFalse(result);
    }
}