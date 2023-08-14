package com.example.springredisdemo.util;

import com.example.springredisdemo.dto.RequestDescriptor;
import com.example.springredisdemo.model.RateLimitRule;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RuleUtil {

    public List<RateLimitRule> findAllMatchedRules(final List<RateLimitRule> rateLimitRules,
                                                   final RequestDescriptor descriptor) {
        return rateLimitRules.stream().filter(rl ->
                (rl.getAccountId() != null && Strings.isEmpty(rl.getAccountId())) || (descriptor.getAccountId()!=null && Objects.equals(rl.getAccountId(), descriptor.getAccountId())) ||
                        (rl.getClientIp() != null && Strings.isEmpty(rl.getClientIp())) || (descriptor.getClientIp()!=null && Objects.equals(rl.getClientIp(), descriptor.getClientIp())) ||
                        (rl.getRequestType() != null && Strings.isEmpty(rl.getRequestType())) || (descriptor.getRequestType()!=null && Objects.equals(rl.getRequestType(), descriptor.getRequestType()))).collect(Collectors.toList());


    }
}
