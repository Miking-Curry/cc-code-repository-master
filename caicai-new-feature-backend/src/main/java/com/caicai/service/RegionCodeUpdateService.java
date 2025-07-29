//package com.caicai.service;
//
//import com.caicai.entity.pojo.Post;
//
///**
// * 行政区划解码服务接口
// * 负责将邮政编码解析成对应的省市区信息
// */
//public interface RegionCodeUpdateService {
//
//    /**
//     * 从province_area解码信息并更新到province字段
//     * 从area解码信息并更新到city字段（去掉省份）
//     * 从state_area解码信息并更新到state字段（去掉省市）
//     *
//     * @return 更新的记录数
//     */
//    int updateRegionFields();
//
//    /**
//     * 处理单条记录的区域信息
//     *
//     * @param id post表的主键ID
//     * @return 是否更新成功
//     */
//    boolean updateSinglePostRegion(Long id);
//
//    /**
//     * 处理单个Post对象的区域信息（主要用于测试）
//     *
//     * @param post Post对象
//     * @return 是否处理成功
//     */
//    boolean processSinglePost(Post post);
//
//    /**
//     * 更新结果统计类
//     */
//    class UpdateResult {
//        private int total;
//        private int success;
//        private int failed;
//
//        public UpdateResult(int total, int success, int failed) {
//            this.total = total;
//            this.success = success;
//            this.failed = failed;
//        }
//
//        public int getTotal() { return total; }
//        public int getSuccess() { return success; }
//        public int getFailed() { return failed; }
//
//        @Override
//        public String toString() {
//            return "总记录数: " + total + ", 更新成功: " + success + ", 更新失败: " + failed;
//        }
//    }
//}