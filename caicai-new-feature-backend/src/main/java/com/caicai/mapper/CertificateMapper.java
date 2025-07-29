package com.caicai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.model.resume.CertificateModel;
import com.caicai.entity.pojo.Certificate;
import com.caicai.entity.vo.CertificateInfoVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CertificateMapper extends BaseMapper<Certificate> {

    /**
     * 根据用户ID查询该用户的所有技能证书列表（仅包含证书名称和图片URL）。
     * 
     * @param userId 用户ID
     * @return 包含证书名称和图片URL的VO对象列表；如果用户没有证书，则返回空列表。
     */
    List<CertificateInfoVO> selectCertificateInfoListByUserId(@Param("userId") Long userId);

    /**
     * @param userId:
     * @return List<Certificate>:
     * @Author: Panda
     * @Description: 根据人才id查询所有资格证书 包含基本
     * @Date: 2025/6/24 15:46
     */
    List<CertificateModel> selectCertificateModelListByUserId(@Param("userId") Long userId);
}
