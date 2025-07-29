package com.caicai;

import com.caicai.entity.model.TalentRatingResult;
import com.caicai.entity.model.TalentResumeModel;
import com.caicai.service.ResumeService;
import com.caicai.util.TalentResume2StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName: TalentResumeTest
 * @Description: 人才简历详情测试类
 * @Author: PCT
 * @Date: 2025/6/25 9:39
 * @Version: 1.0
 */

@SpringBootTest
public class TalentResumeTest {
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private TalentResume2StringUtil talentResume2StringUtil;

    @Test
    public void testGetTalentResumeByTalentId() {
        TalentResumeModel talentResumeDetail = resumeService.getTalentResumeDetail(4896L);
        String result = talentResume2StringUtil.getTalentResumeModel2String(talentResumeDetail);
        System.out.println(result);
    }
}
