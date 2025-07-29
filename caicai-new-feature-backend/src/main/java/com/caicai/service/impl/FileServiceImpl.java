package com.caicai.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.caicai.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: YangFuyi
 * @Description:文件服务具体实现的逻辑
 * @CreateDate: 2025-05-22 10:56
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    //  允许上传的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif",
            "image/jpg", "image/webp", "image/bmp", "image/svg+xml");
    //  允许上传的文件类型
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    //  限制图片上传的大小(5MB)
    private static final long MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    //  限制文件上传的大小(20MB)
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 20;
    //  阿里云OSS客户端
    @Autowired
    private OSS ossClient;
    // 存储桶名称
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    // 访问域名
    @Value("${aliyun.oss.access-url}")
    private String accessUrl;

    /**
     * @Description: 上传图片到阿里云OSS
     * @param file 文件
     * @param type 文件类型
     * @return
     * @throws Exception
     */
    @Override
    public String uploadImageFile(MultipartFile file, String type) throws Exception {
        //检查文件参数
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        //检查文件类型
        String contentType = file.getContentType();
        if (contentType == null||!ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("不支持图片类型"+contentType);
        }
        //检查文件大小
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("图片大小不能超过"+(MAX_IMAGE_SIZE)/1024/1024+"MB");
        }
        //上传到阿里云OSS
        String directory="ccn-idcard-data";
        if (StringUtils.hasText(type))
        {
            directory+="/"+type;
        }
        return uploadToOss(file,directory);
    }
    /**
     * @Description: 上传文件到阿里云OSS
     * @param file 文件对象
     * @param type 文件类型
     * @return 文件访问的URL
     */

    @Override
    public String uploadDocumentFile(MultipartFile file, String type) throws Exception {
        //检查文件参数
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        //检查文件类型
        String contentType = file.getContentType();
        if (contentType == null||!ALLOWED_FILE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("不支持文件类型"+contentType);
        }
        //检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过"+(MAX_FILE_SIZE)/1024/1024+"MB");
        }
        //上传到阿里云OSS
        String directory="ccn-idcard-data";
        if (StringUtils.hasText(type))
        {
            directory+="/"+type;
        }
        return uploadToOss(file,directory);
    }

    /**
     * @Description: 删除文件
     * @param fileUrl 文件访问的URL
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean deleteFile(String fileUrl) {
        if(!StringUtils.hasText(fileUrl))
        {
            logger.error("文件路径不能为空");
            return false;
        }
        try{
            //从URL中提取对象key
            String objectKey=extractObjectKeyFromUrl(fileUrl);
            if(!StringUtils.hasText(objectKey))
            {
                logger.error("无法从URL中提取对象key: {}"+fileUrl);
                return false;
            }
            //删除OSS中的文件
            ossClient.deleteObject(bucketName,objectKey);
            logger.info("成功删除文件: {}",objectKey);
            return true;
        }
        catch (Exception e)
        {
            logger.error("删除文件时发送异常：{}",e.getMessage(),e);
            return false;
        }
    }
    /**
     * @Description: 上传文档文件到阿里云OSS
     * @param file 文件对象
     * @param directory 文件上传的目录
     * @return 文件访问的URL
     */
    private String uploadToOss(MultipartFile file, String directory) {
        //获取文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName==null)
        {
            originalFileName="unknown_file";
        }
        //生成文件的扩展名
        String extension="";
        int dotIndex=originalFileName.lastIndexOf(".");
        if(dotIndex>0)
        {
            extension=originalFileName.substring(dotIndex+1);
        }
        String fileName= System.currentTimeMillis()+"_"+ UUID.randomUUID().toString().substring(0,8)+ (extension.isEmpty() ? "" : "." + extension);
        //按日期创建目录
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        String dataPath=dateFormat.format(new Date());
        //构建对象Key(OSS完整路径）
        String objectKey = directory + (directory.endsWith("/") ? "" : "/") + dataPath + "/" + fileName;
        //构建文件元数据
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try(InputStream inputStream=file.getInputStream()){
            ossClient.putObject(bucketName,objectKey,inputStream,metadata);
            logger.info("成功上传文件：{}",objectKey);
            return buildFileUrl(objectKey);
        }
        catch (Exception e)
        {
            logger.error("上传文件时发送异常：{}",e.getMessage(),e);
            throw new RuntimeException("上传文件时发送异常："+e.getMessage(),e);
        }
    }
    /**
     * @Description: 构建文件访问URL
     * @param objectKey 文件对象Key
     * @return 文件访问URL
     */
    private String buildFileUrl(String objectKey) {
        return accessUrl+"/"+objectKey;
    }
    /**
     * @Description: 从文件访问URL中提取对象Key
     * @param fileUrl 文件访问URL
     * @return 对象Key
     */
    private String extractObjectKeyFromUrl(String fileUrl) {
        if(fileUrl.startsWith(accessUrl)){
            //  文件路径是以访问URL开头
            return fileUrl.substring(accessUrl.length()+1);
        }else{
            return  fileUrl;
        }
    }
}
