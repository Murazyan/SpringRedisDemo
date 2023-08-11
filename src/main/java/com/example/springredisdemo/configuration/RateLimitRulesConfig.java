package com.example.springredisdemo.configuration;

import com.example.springredisdemo.model.RateLimitRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(ignoreInvalidFields = true)
@PropertySource(value = "classpath:ratelimitrules.yml", factory = YamlPropertySourceFactory.class)

public class RateLimitRulesConfig {

    private List<RateLimitRule> rateLimitRules;

    public List<RateLimitRule> getRateLimitRules() {
        return rateLimitRules;
    }

    public void setRateLimitRules(List<RateLimitRule> rateLimitRules) {
        this.rateLimitRules = rateLimitRules;
    }
}