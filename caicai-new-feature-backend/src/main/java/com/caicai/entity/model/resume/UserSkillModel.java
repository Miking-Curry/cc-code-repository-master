package com.caicai.entity.model.resume;

import lombok.Data;

/**
 * @ClassName: UserSkillModel
 * @Description:
 * @Author: PCT
 * @Date: 2025/6/27 14:33
 * @Version: 1.0
 */

@Data
public class UserSkillModel {
    private Long id;
    private String skillName;
    private String skillProficiency;
    private String skillTime;
}
