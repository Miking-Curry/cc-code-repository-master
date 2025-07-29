//package com.caicai.runner;
//
//import cn.hutool.core.util.ArrayUtil;
//import com.caicai.service.RegionCodeUpdateService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
///**
// * 区域更新启动器
// * 可以通过启动参数 --update-region-fields=true 触发区域信息更新
// */
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RegionUpdateRunner implements CommandLineRunner {
//
//    private final RegionCodeUpdateService regionCodeUpdateService;
//
//    @Override
//    public void run(String... args) {
//        // 检查是否有更新区域的命令行参数
//        boolean shouldUpdate = ArrayUtil.contains(args, "--update-region-fields=true");
//
//        if (shouldUpdate) {
//            log.info("系统启动时开始执行区域信息更新...");
//            try {
//                int count = regionCodeUpdateService.updateRegionFields();
//                log.info("区域信息更新完成，共更新 {} 条记录", count);
//            } catch (Exception e) {
//                log.error("区域信息更新失败", e);
//            }
//        }
//    }
//}