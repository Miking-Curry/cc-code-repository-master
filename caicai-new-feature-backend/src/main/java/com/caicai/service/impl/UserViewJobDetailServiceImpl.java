package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.dto.JobViewDTO;
import com.caicai.entity.pojo.UserViewJobDetail;
import com.caicai.service.UserViewJobDetailService;
import com.caicai.mapper.UserViewJobDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author ccn00
* @description 针对表【user_view_job_detail(用户查看岗位详情表)】的数据库操作Service实现
* @createDate 2025-05-21 17:35:43
*/
@Service
public class UserViewJobDetailServiceImpl extends ServiceImpl<UserViewJobDetailMapper, UserViewJobDetail>
        implements UserViewJobDetailService {

    /**
     * @description: 浏览岗位详情退出后的数据采集业务
     * @author: LiWeihang
     * @create: 2025/5/21 17:40
     **/
    @Override
    public Boolean saveJobView(Long userId, JobViewDTO jobViewDTO) {
        return this.save(new UserViewJobDetail()
                .setUserId(userId)
                .setPostId(jobViewDTO.getPostId())
                .setViewTime(jobViewDTO.getViewTime())
                .setMinSalary(jobViewDTO.getMinSalary())
                .setMaxSalary(jobViewDTO.getMaxSalary())
                .setIndustryCategory(jobViewDTO.getIndustryCategory())
        );
    }
}




