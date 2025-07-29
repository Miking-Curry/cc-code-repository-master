package com.caicai.entity.dto;

import com.caicai.entity.pojo.Company;
import com.caicai.entity.pojo.Post;
import lombok.Data;



/**
 * @Description:岗位信息的DTO
 * @Author: Yang
 * @Date: 2025/6/13 9:43
 */
@Data
public class PostDTO {
    //岗位信息
    private Post posts;
    //公司信息
    private Company companies;
}
