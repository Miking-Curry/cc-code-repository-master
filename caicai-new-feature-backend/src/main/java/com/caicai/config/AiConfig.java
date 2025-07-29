package com.caicai.config;

import com.caicai.constant.PromptConstant;
import org.springframework.ai.autoconfigure.vectorstore.redis.RedisVectorStoreAutoConfiguration;
import org.springframework.ai.autoconfigure.vectorstore.redis.RedisVectorStoreProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 对AI的一些配置
 * @author: LiWeihang
 * @create: 2025/5/23 9:23
 **/
@Configuration
public class AiConfig {

    /**
    @description: 配置总结岗位信息的Client
    @author: LiWeihang
    @create: 2025/5/23 9:25
    **/
    @Bean
    public ChatClient summarizeJobInfoClient(OpenAiChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(PromptConstant.JOB_DESC_SUMMARY_PROMPT)
                .build();
    }
    /**
    @description: 配置总结给简历打分的Client
    @author: Yang
    @create: 2025/6/13 11:20
    **/
    @Bean
    public ChatClient scoreResumeClient(OpenAiChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(PromptConstant.RESUME_SCORE_PROMPT)
                .build();
    }
    /**
    @description: 配置总结简历的Client
    @author: Yang
    @create: 2025/6/19 9:05
    **/
    @Bean
    public ChatClient summarizeResumeClient(OpenAiChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(PromptConstant.RESUME_SUMMARY_PROMPT)
                .build();
    }

    /**
     * @param model:
     * @return ChatClient:
     * @Author: Panda
     * @Description: 配置AI人才评分的Client
     * @Date: 2025/6/25 16:55
     */
    @Bean(name = "talentRatingClient", value = "talentRatingClient")
    public ChatClient talentRatingClient(OpenAiChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem(PromptConstant.TALENT_RATING_PROMPT)
                .build();
    }

}
