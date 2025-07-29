package com.caicai.service.impl;

import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Company;
import com.caicai.entity.pojo.Post;
import com.caicai.service.JobSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobSummaryServiceImpl implements JobSummaryService {
    private final ChatClient summarizeJobInfoClient;

    /**
     * 流式返回工作岗位摘要
     *
     * @param postDTO 岗位信息
     * @return 流式返回的岗位摘要
     */
    @Override
    public Flux<String> streamJobSummary(PostDTO postDTO) {
        if(postDTO == null)
        {
            log.warn("岗位信息为空，无法进行AI摘要分析");
            return Flux.empty();
        }
        try{
            String messageContent = buildPostMessage(postDTO);
            log.info("开始对岗位ID: {} 的岗位进行流式AI摘要分析", postDTO.getPosts().getId());

            //使用流式 API
            return summarizeJobInfoClient
                    .prompt()
                    .user(messageContent)
                    .stream()
                    .content()
                    .concatWith(Mono.just("[DONE]"));
        }
        catch (Exception e)
        {
            log.error("流式AI摘要分析过程中发生错误", e);
            return Flux.empty();
        }
    }

    /**
     * 阻塞式返回工作岗位摘要
     *
     * @param postDTO 岗位信息
     * @return 岗位摘要
     */
    @Override
    public String generateJobSummary(PostDTO postDTO) {
        if(postDTO == null)
        {
            log.warn("岗位信息为空，无法进行AI摘要分析");
            return "无岗位信息";
        }
        try{
            String messageContent = buildPostMessage(postDTO);
            log.info("开始对岗位ID: {} 的岗位进行阻塞式AI摘要分析", postDTO.getPosts().getId());

            // 使用阻塞式 API - 适配Spring AI 1.0.0-M6版本
            return summarizeJobInfoClient
                    .prompt()
                    .user(messageContent)
                    .call()
                    .content();
        }
        catch (Exception e)
        {
            log.error("生成岗位摘要时发生错误: {}", e.getMessage(), e);
            return "系统正在升级中，请稍后再试";
        }
    }

    /**
     * 构建工作岗位信息
     *
     * @param postDTO
     * @return
     */
    private String buildPostMessage(PostDTO postDTO) {
        if (postDTO == null || postDTO.getPosts() == null || postDTO.getCompanies() == null) {
            return "无岗位信息";
        }

        Post post = postDTO.getPosts();
        Company company = postDTO.getCompanies();

        StringBuilder message = new StringBuilder();

        // 岗位基本信息
        message.append("【岗位名称】").append(post.getName()).append("\n\n");

        // 薪资信息
        if (post.getMinSalary() != null && post.getMaxSalary() != null) {
            message.append("【薪资范围】").append(post.getMinSalary()).append("K-").append(post.getMaxSalary()).append("K").append("\n\n");
        } else if (post.getMinSalary() != null) {
            message.append("【薪资范围】").append(post.getMinSalary()).append("K以上").append("\n\n");
        } else if (post.getMaxSalary() != null) {
            message.append("【薪资范围】").append("最高").append(post.getMaxSalary()).append("K").append("\n\n");
        }

        // 工作地点
        message.append("【工作地点】");
        if (post.getProvince() != null && post.getCity() != null) {
            message.append(post.getProvince()).append("-").append(post.getCity());
        } else if (post.getCity() != null) {
            message.append(post.getCity());
        }
        if (post.getAddress() != null) {
            message.append(" ").append(post.getAddress());
        }
        message.append("\n\n");

        // 招聘人数
        if (post.getNeedNum() != null) {
            message.append("【招聘人数】").append(post.getNeedNum()).append("人").append("\n\n");
        }

        // 学历要求
        if (post.getEducation() != null) {
            message.append("【学历要求】").append(com.caicai.enums.EducationType.getDescByCode(post.getEducation())).append("\n\n");
        }

        // 经验要求
        message.append("【经验要求】");
        if (post.getMinExperience() != null && post.getMaxExperience() != null) {
            message.append(post.getMinExperience()).append("-").append(post.getMaxExperience()).append("年");
        } else if (post.getMinExperience() != null) {
            message.append(post.getMinExperience()).append("年以上");
        } else if (post.getMaxExperience() != null) {
            message.append(post.getMaxExperience()).append("年以内");
        } else {
            message.append("不限");
        }
        message.append("\n\n");

        // 岗位类型
        if (post.getPostType() != null) {
            message.append("【岗位类型】").append(post.getPostType()).append("\n\n");
        }

        // 所属部门
        if (post.getDepartment() != null) {
            message.append("【所属部门】").append(post.getDepartment()).append("\n\n");
        }

        // 岗位描述
        if (post.getDesc() != null) {
            message.append("【岗位描述】\n").append(post.getDesc()).append("\n\n");
        }

        // 公司信息
        message.append("【公司信息】\n");
        message.append("公司名称：").append(company.getName()).append("\n");

        if (company.getIndustry() != null) {
            message.append("所属行业：").append(company.getIndustry()).append("\n");
        }

        if (company.getStaffSize() != null) {
            message.append("公司规模：").append(company.getStaffSize()).append("人").append("\n");
        }

        if (company.getType() != null) {
            message.append("公司类型：").append(company.getType()).append("\n");
        }

        if (company.getIntroduction() != null) {
            message.append("公司简介：").append(company.getIntroduction()).append("\n");
        }

        if (company.getWebsite() != null) {
            message.append("公司网站：").append(company.getWebsite()).append("\n");
        }

        return message.toString();
    }
} 