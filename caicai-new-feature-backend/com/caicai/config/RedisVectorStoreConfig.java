package com.caicai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
@Slf4j
public class RedisVectorStoreConfig {

    @Value("${spring.ai.vectorstore.redis.index-name:caicai}")
    private String indexName;

    @Value("${spring.ai.vectorstore.redis.prefix:caicai:post}")
    private String prefix;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:#{null}}")
    private String redisPassword;

    @Bean
    public JedisPooled jedisPooled() {
        log.info("Redis配置信息 - host: {}, port: {}, password: {}, indexName: {}, prefix: {}", 
                redisHost, redisPort, redisPassword != null ? "已设置" : "未设置", indexName, prefix);
        
        if (redisPassword != null && !redisPassword.isEmpty()) {
            return new JedisPooled(redisHost, redisPort, null, redisPassword);
        } else {
            return new JedisPooled(redisHost, redisPort);
        }
    }

    @Bean
    public RedisVectorStore redisVectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        log.info("初始化RedisVectorStore - 索引名: {}, 前缀: {}", indexName, prefix);
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(indexName)
                .prefix(prefix)
                .initializeSchema(true)
                .build();
    }
} 