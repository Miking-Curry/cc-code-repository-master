//package com.caicai.controller;
//
//import com.caicai.result.R;
//import com.caicai.service.RegionCodeUpdateService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.CompletableFuture;
//
///**
// * 区域信息更新控制器
// */
//@Controller
//@RequestMapping("/region")
//@RequiredArgsConstructor
//@Slf4j
//public class RegionUpdateController {
//
//    private final RegionCodeUpdateService regionCodeUpdateService;
//
//    /**
//     * 显示区域信息更新页面
//     */
//    @GetMapping("/update-page")
//    public String showUpdatePage(Model model) {
//        return "region-update";
//    }
//
//    /**
//     * 同步更新post表的区域信息 - REST API
//     */
//    @PostMapping("/update-region-fields")
//    @ResponseBody
//    public R<String> updateRegionFields() {
//        log.info("开始同步更新区域字段");
//        int count = regionCodeUpdateService.updateRegionFields();
//        return R.success("更新成功，共更新 " + count + " 条记录");
//    }
//
//    /**
//     * 同步更新post表的区域信息 - GET方式（方便直接在浏览器调用）
//     */
//    @GetMapping("/update-all")
//    @ResponseBody
//    public R<String> updateAll() {
//        log.info("开始通过GET请求更新所有区域字段");
//        int count = regionCodeUpdateService.updateRegionFields();
//        return R.success("更新成功，共更新 " + count + " 条记录");
//    }
//
//    /**
//     * 异步更新post表的区域信息
//     */
//    @PostMapping("/update-region-fields-async")
//    @ResponseBody
//    public R<String> updateRegionFieldsAsync() {
//        log.info("开始异步更新区域字段");
//        // 异步执行更新，避免HTTP请求超时
//        CompletableFuture.runAsync(() -> {
//            try {
//                int count = regionCodeUpdateService.updateRegionFields();
//                log.info("异步更新区域字段完成，共更新 {} 条记录", count);
//            } catch (Exception e) {
//                log.error("异步更新区域字段失败", e);
//            }
//        });
//        return R.success("更新任务已启动，请稍后查看系统日志了解进度");
//    }
//
//    /**
//     * 更新单个post记录的区域信息
//     */
//    @PostMapping("/update-single-region")
//    @ResponseBody
//    public R<String> updateSingleRegion(@RequestParam("id") Long id) {
//        log.info("开始更新ID为 {} 的记录区域信息", id);
//        boolean success = regionCodeUpdateService.updateSinglePostRegion(id);
//        if (success) {
//            return R.success("记录更新成功");
//        } else {
//            return R.error("记录更新失败");
//        }
//    }
//
//    /**
//     * 更新单个post记录的区域信息 - GET方式（方便直接在浏览器调用）
//     */
//    @GetMapping("/update-one")
//    @ResponseBody
//    public R<String> updateOne(@RequestParam("id") Long id) {
//        log.info("开始通过GET请求更新ID为 {} 的记录区域信息", id);
//        boolean success = regionCodeUpdateService.updateSinglePostRegion(id);
//        if (success) {
//            return R.success("记录更新成功");
//        } else {
//            return R.error("记录更新失败");
//        }
//    }
//}