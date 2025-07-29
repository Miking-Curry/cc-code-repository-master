package com.caicai.service;

import com.caicai.entity.dto.JobViewDTO;
import com.caicai.entity.pojo.UserViewJobDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ccn00
* @description 针对表【user_view_job_detail(用户查看岗位详情表)】的数据库操作Service
* @createDate 2025-05-21 17:35:43
*/
public interface UserViewJobDetailService extends IService<UserViewJobDetail> {

    /**
    @description: 浏览岗位详情退出后的数据采集业务
    @author: LiWeihang
    @create: 2025/5/21 17:42
    **/
    Boolean saveJobView(Long userId, JobViewDTO jobViewDTO);
}
