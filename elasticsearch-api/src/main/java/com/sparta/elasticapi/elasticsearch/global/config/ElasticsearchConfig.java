package com.sparta.elasticapi.elasticsearch.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.sparta.elasticapi.elasticsearch.domain.post.repository")
public class ElasticsearchConfig {
}
