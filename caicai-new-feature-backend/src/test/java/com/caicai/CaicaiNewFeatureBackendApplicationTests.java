package com.caicai;

import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.entity.pojo.User;
import com.caicai.service.ResumeService;
import com.caicai.util.JwtUtil;
import com.caicai.util.RegxUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class CaicaiNewFeatureBackendApplicationTests {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private ResumeService  resumeService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testPhone() {
        System.out.println(RegxUtil.checkPhone("13597851487"));
    }

    @Test
    public void testToken()
    {
        String token= JwtUtil.generateToken("4862");
        System.out.println(token);
    }

  /*  @Test
    public void testToken2()
    {
        System.out.println(JwtUtil.parseToken("eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoiMSIsImlhdCI6MTc0ODU4NjY1OSwiZXhwIjoxNzQ4NTkwMjU5fQ.ry-OlPkwXApqjNlDr2KQ027I517Lsa_JTu18PU2AcAdJff_ThCFXeQjeHSW2oYyCiFNsZH2kEtoFw5nVME-OXw"));
        System.out.println(JwtUtil.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX2lkIjoiMSIsImlhdCI6MTc0ODU4NjY1OSwiZXhwIjoxNzQ4NTkwMjU5fQ.ry-OlPkwXApqjNlDr2KQ027I517Lsa_JTu18PU2AcAdJff_ThCFXeQjeHSW2oYyCiFNsZH2kEtoFw5nVME-OXw"));
    }*/

  @Test
  public void testVectorStore() {
      log.info("开始查询向量数据库");
      try {
          // 使用更简短的查询文本，或保留原来的长文本
          String query = "实习生 市场调研 客户需求 深圳市芯斐电子";
          // 或保留你原来的长查询文本

          SearchRequest request = SearchRequest.builder()
                  .query("")
                  .topK(5)
                  .similarityThreshold(0f)
                  .build();

          List<Document> results = vectorStore.similaritySearch(request);

          log.info("查询结果数量: {}", results.size());

          // 更结构化的输出结果
          for (Document result : results) {
              log.info("---文档信息开始---");
              log.info("文档ID: {}", result.getId());
              log.info("文档内容: {}", result.getText().substring(0, Math.min(100, result.getText().length())) + "...");
              if (result.getMetadata().containsKey("similarity_score")) {
                  log.info("相似度分数: {}", result.getMetadata().get("similarity_score"));
              }
              log.info("---文档信息结束---");
          }
      } catch (Exception e) {
          log.error("向量查询失败: {}", e.getMessage(), e);
      }
      log.info("结束查询向量数据库");
  }

    @Test
    public void testDirectWrite() {
        try {
            // 创建一个简单的测试文档
            Document testDoc = new Document(
                    "test:1",
                    "这是一个测试文档",
                    Map.of("name", "测试", "company", "测试公司")
            );

            // 直接添加到向量库
            vectorStore.add(Collections.singletonList(testDoc));

            log.info("测试文档写入成功");

            // 尝试读取
            SearchRequest request = SearchRequest.builder()
                    .query("测试")
                    .topK(5)
                    .build();

            List<Document> results = vectorStore.similaritySearch(request);
            log.info("查询结果数量: {}", results.size());

        } catch (Exception e) {
            log.error("测试写入失败: {}", e.getMessage(), e);
        }
    }
    
    @Test
    public void testSearch() {
      ResumeDetailDTO resumeDetailDTO = resumeService.getResumeDetail(1l);
      log.info("简历信息: {}", resumeDetailDTO);
    }
}
