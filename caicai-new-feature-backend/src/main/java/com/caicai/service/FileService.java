package com.caicai.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: YangFuyi
 * @Description: 文件服务
 * @CreateDate: 2025-05-21 15:37
 */
public interface FileService {
    /**
     * 上传图片文件
     *
     * @param file 文件
     * @param type 文件类型
     * @return 文件访问的URL
     */
    String uploadImageFile(MultipartFile file, String type) throws Exception;
    /**
     * 上传文档文件
     *
     * @param file 文件
     * @param type 文件类型
     * @return 文件访问的URL
     */
    String uploadDocumentFile(MultipartFile file, String type) throws Exception;
    /**
     * 删除文件
     *
     * @param fileUrl 文件访问的URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
}
