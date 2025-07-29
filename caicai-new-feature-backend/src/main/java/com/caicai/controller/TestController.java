package com.caicai.controller;

import com.caicai.entity.pojo.User;
import com.caicai.result.R;
import com.caicai.service.FileService;
import com.caicai.service.UserExtensionService;
import com.caicai.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 测试的Controller
 * @author: LiWeihang
 * @create: 2025/5/16 11:32
 **/
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    private final UserExtensionService userExtensionService;

    private final FileService FileService;

    /**
     * @description: 因为已经在过滤器里把userId存到请求属性了，可以用可以用@RequestAttribute("userId")获取userId
     * @author: LiWeihang
     * @create: 2025/5/16 11:33
     */
    @GetMapping("/JWT")
    public R<String> testMethod(@RequestAttribute("userId") Long userId, @RequestAttribute("claims") Claims claims) {
        return R.success(userId + ":" + claims);
    }

    @GetMapping("/user")
    public R<User> getUser(@RequestParam("userId") Long userId) {
        return R.success(userService.getById(userId));
    }

    @GetMapping("/point")
    public R<Long> getUserPoint(@RequestParam("userId") Long userId) {
        return R.success(userExtensionService.getPointByUserId(userId));
    }

    @GetMapping("/point/token")
    public R<Long> getUserPointByToken(@RequestAttribute("userId") Long userId) {
        return R.success(userExtensionService.getPointByUserId(userId));
    }

    @PostMapping("/upload/image")
    public R<String> uploadImage(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "type",required = false) String type,
                                 @RequestAttribute(value = "userId",required = false) Long userId)
    {
        try {
            //如果有userId，可以把其作为文件标识
            String fileType=userId != null ? userId.toString() : type;
            String imageUrl=FileService.uploadImageFile(file, fileType);
            return R.success(imageUrl);
        } catch (Exception e) {
            return R.error("图片上传失败"+e.getMessage());
        }
    }
    @PostMapping("/upload/document")
    public R<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "type",required = false) String type,
                                    @RequestAttribute(value = "userId",required = false) Long userId)
    {
        try {
            //如果有userId，可以把其作为文件标识
            String fileType=userId != null ? userId.toString() : type;
            String documentUrl=FileService.uploadDocumentFile(file, fileType);
            return R.success(documentUrl);
        } catch (Exception e) {
            return R.error("文档上传失败"+e.getMessage());
        }
    }
    @DeleteMapping("/delete/file")
    public R<String> deleteFile(@RequestParam("fileUrl") String fileUrl){
        boolean  result=FileService.deleteFile(fileUrl);
        if(result) {
            return R.success("文件删除成功");
        }
        else {
            return R.error("文件删除失败");
        }
    }

}
