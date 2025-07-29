package com.caicai.config;

import lombok.Data;
import org.springframework.ai.autoconfigure.vectorstore.redis.RedisVectorStoreProperties;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

import static org.springframework.ai.document.MetadataMode.EMBED;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@Data
public class RedisVectorStoreConfig {

    @Value("${spring.ai.vectorstore.redis.index}")
    private String indexName;

    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String prefix;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:#{null}}")
    private String redisPassword;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.base-url:https://dashscope.aliyuncs.com/compatible-mode}")
    private String baseUrl;

    @Bean
    public JedisPooled jedisPooled() {
        if (redisPassword != null && !redisPassword.isEmpty()) {
            return new JedisPooled(redisHost, redisPort, null, redisPassword);
        } else {
            return new JedisPooled(redisHost, redisPort);
        }
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel, JedisPooled jedisPooled) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(indexName)
                .prefix(prefix)
                .initializeSchema(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        System.out.println("使用的baseUrl是: " + baseUrl);
        // 使用builder方式创建API实例
        OpenAiApi api = OpenAiApi.builder()
            .baseUrl(baseUrl)
            .apiKey(apiKey)
            .build();

        return new OpenAiEmbeddingModel(api, EMBED, OpenAiEmbeddingOptions.builder()
                .model("text-embedding-v4")
                .dimensions(1536)
                .build());
    }
}
