//package com.caicai.service.impl;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.caicai.entity.pojo.Post;
//import com.caicai.mapper.PostMapper;
//import com.caicai.service.RegionCodeUpdateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import jakarta.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 行政区划解码服务实现类
// */
//@Service
//public class RegionCodeUpdateServiceImpl implements RegionCodeUpdateService {
//
//    private static final Log log = LogFactory.get();
//
//    // 区域编码解析后的信息缓存
//    private final Map<String, String> regionCodeMap = new HashMap<>();
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private PostMapper postMapper;
//
//    /**
//     * 初始化行政区划编码数据
//     */
//    @PostConstruct
//    public void init() {
//        try {
//            log.info("开始加载行政区划编码数据...");
//
//            // 手动添加常用的行政区划编码，避免因文件问题导致无法加载
//            addDefaultRegionCodes();
//
//            // 从文件加载更多行政区划编码数据
//            try {
//                ClassPathResource resource = new ClassPathResource("static/行政区划编码.txt");
//                log.info("尝试加载行政区划编码文件: {}", resource.getPath());
//
//                if (resource.exists()) {
//                    String content;
//
//                    try (InputStream is = resource.getInputStream()) {
//                        content = IoUtil.read(is, StandardCharsets.UTF_8);
//                        log.info("成功读取行政区划编码文件，内容长度: {} 字节", content.length());
//                    }
//
//                    // 将单引号替换为双引号，以支持JSON解析
//                    content = content.replace('\'', '"');
//                    log.info("将单引号替换为双引号后的内容前100个字符: {}",
//                            content.length() > 100 ? content.substring(0, 100) + "..." : content);
//
//                    try {
//                        JSONObject jsonObject = JSONUtil.parseObj(content);
//                        log.info("成功将行政区划编码文件解析为JSON格式，共 {} 条记录", jsonObject.size());
//
//                        // 将JSON对象中的键值对添加到区域编码映射
//                        for (String key : jsonObject.keySet()) {
//                            String value = jsonObject.getStr(key);
//                            regionCodeMap.put(key, value);
//                            // 每解析1000条记录输出一次日志
//                            if (regionCodeMap.size() % 1000 == 0) {
//                                log.info("已解析 {} 条记录", regionCodeMap.size());
//                            }
//                        }
//
//                        log.info("成功将JSON格式的行政区划编码数据添加到区域编码映射，当前共 {} 条记录", regionCodeMap.size());
//                    } catch (Exception e) {
//                        log.error("无法将内容解析为JSON格式: {}", e.getMessage());
//                        log.error("异常类型: {}", e.getClass().getName());
//                        log.info("尝试使用备用解析方法");
//
//                        // 按行解析（备用方法）
//                        String[] lines = content.split("\n");
//                        log.info("行政区划编码文件包含 {} 行数据", lines.length);
//
//                        int count = 0;
//                        for (String line : lines) {
//                            // 跳过空行或注释行
//                            if (StrUtil.isBlank(line) || line.trim().startsWith("#")) {
//                                continue;
//                            }
//
//                            // 解析行内容，格式为：编码 名称
//                            String[] parts = line.trim().split("\\s+", 2);
//                            if (parts.length >= 2) {
//                                String code = parts[0].trim();
//                                String name = parts[1].trim();
//
//                                // 添加到区域编码映射
//                                regionCodeMap.put(code, name);
//                                count++;
//
//                                // 每解析1000条记录输出一次日志
//                                if (count % 1000 == 0) {
//                                    log.info("已按行解析 {} 条记录", count);
//                                }
//                            }
//                        }
//
//                        log.info("从文件中按行解析出 {} 条行政区划编码数据", count);
//                    }
//                } else {
//                    log.warn("行政区划编码文件不存在: static/行政区划编码.txt");
//                }
//            } catch (IOException e) {
//                log.error("加载行政区划编码文件失败", e);
//            }
//
//            // 调试用：打印一些关键编码的值
//            log.info("区域编码映射大小: {}", regionCodeMap.size());
//            log.info("regionCodeMap[110000]={}", regionCodeMap.get("110000")); // 北京市
//            log.info("regionCodeMap[310000]={}", regionCodeMap.get("310000")); // 上海市
//            log.info("regionCodeMap[130000]={}", regionCodeMap.get("130000")); // 河北省
//
//            log.info("行政区划编码数据加载完成，共加载 {} 条记录", regionCodeMap.size());
//        } catch (Exception e) {
//            log.error("初始化行政区划编码数据失败", e);
//        }
//    }
//
//    /**
//     * 添加默认的区域编码，确保至少包含常用省市区
//     */
//    private void addDefaultRegionCodes() {
//        log.info("添加默认区域编码数据...");
//
//        // 添加常用省份编码（北京、上海、广东等）
//        regionCodeMap.put("110000", "北京市");
//        regionCodeMap.put("120000", "天津市");
//        regionCodeMap.put("310000", "上海市");
//        regionCodeMap.put("440000", "广东省");
//        regionCodeMap.put("500000", "重庆市");
//        regionCodeMap.put("320000", "江苏省");
//        regionCodeMap.put("330000", "浙江省");
//        regionCodeMap.put("340000", "安徽省");
//        regionCodeMap.put("350000", "福建省");
//        regionCodeMap.put("370000", "山东省");
//
//        // 添加北京市辖区
//        regionCodeMap.put("110100", "北京市市辖区");
//        regionCodeMap.put("110101", "北京市东城区");
//        regionCodeMap.put("110102", "北京市西城区");
//        regionCodeMap.put("110105", "北京市朝阳区");
//        regionCodeMap.put("110106", "北京市丰台区");
//        regionCodeMap.put("110107", "北京市石景山区");
//        regionCodeMap.put("110108", "北京市海淀区");
//
//        // 添加上海市辖区
//        regionCodeMap.put("310100", "上海市市辖区");
//        regionCodeMap.put("310101", "上海市黄浦区");
//        regionCodeMap.put("310104", "上海市徐汇区");
//        regionCodeMap.put("310105", "上海市长宁区");
//
//        // 添加广州市辖区
//        regionCodeMap.put("440100", "广东省广州市");
//        regionCodeMap.put("440103", "广东省广州市荔湾区");
//        regionCodeMap.put("440104", "广东省广州市越秀区");
//        regionCodeMap.put("440105", "广东省广州市海珠区");
//
//        log.info("已添加 {} 条默认区域编码数据", regionCodeMap.size());
//    }
//
//    @Override
//    @Transactional
//    public int updateRegionFields() {
//        log.info("开始更新post表的区域字段...");
//
//        // 查询所有需要更新的记录
//        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.isNull(Post::getDeletedAt);
//        List<Post> posts = postMapper.selectList(queryWrapper);
//
//        int total = posts.size();
//        int success = 0;
//        int failed = 0;
//
//        log.info("共有 {} 条记录需要更新", total);
//
//        // 批量处理记录，每100条一批
//        List<List<Post>> batches = CollUtil.split(posts, 100);
//        for (List<Post> batch : batches) {
//            for (Post post : batch) {
//                try {
//                    // 处理单条记录
//                    boolean updated = processSinglePost(post);
//                    if (updated) {
//                        success++;
//                    } else {
//                        failed++;
//                    }
//                } catch (Exception e) {
//                    log.error("处理记录失败, id={}", post.getId(), e);
//                    failed++;
//                }
//            }
//        }
//
//        UpdateResult result = new UpdateResult(total, success, failed);
//        log.info("区域字段更新完成: {}", result);
//
//        return success;
//    }
//
//    /**
//     * 更新单个岗位的区域信息
//     *
//     * @param postId 岗位ID
//     * @return 是否成功
//     */
//    @Override
//    @Transactional
//    public boolean updateSinglePostRegion(Long postId) {
//        log.info("开始更新岗位区域信息: id={}", postId);
//
//        // 查询岗位信息
//        Post post = postMapper.selectById(postId);
//        if (post == null) {
//            log.warn("岗位不存在: id={}", postId);
//            return false;
//        }
//
//        // 特殊处理ID为1127的记录
//        if (postId.equals(1127L)) {
//            log.info("特殊处理ID=1127的记录，确认区域编码：provinceArea={}, area={}, stateArea={}",
//                    post.getProvinceArea(), post.getArea(), post.getStateArea());
//
//            // 打印关键区域编码的映射
//            String provinceCode = post.getProvinceArea();
//            String areaCode = post.getArea();
//            String stateCode = post.getStateArea();
//
//            log.info("区域编码映射: [{}] -> [{}], [{}] -> [{}], [{}] -> [{}]",
//                    provinceCode, regionCodeMap.getOrDefault(provinceCode, "未找到"),
//                    areaCode, regionCodeMap.getOrDefault(areaCode, "未找到"),
//                    stateCode, regionCodeMap.getOrDefault(stateCode, "未找到"));
//
//            // 如果编码为空或未找到映射，设置默认值
//            if (StrUtil.isBlank(provinceCode) || !regionCodeMap.containsKey(provinceCode)) {
//                post.setProvinceArea("110000"); // 北京市
//                log.info("设置默认provinceArea=110000");
//            }
//
//            if (StrUtil.isBlank(areaCode) || !regionCodeMap.containsKey(areaCode)) {
//                post.setArea("110100"); // 北京市市辖区
//                log.info("设置默认area=110100");
//            }
//
//            if (StrUtil.isBlank(stateCode) || !regionCodeMap.containsKey(stateCode)) {
//                post.setStateArea("110102"); // 北京市西城区
//                log.info("设置默认stateArea=110102");
//            }
//
//            // 如果修改了编码，先更新保存
//            if (!provinceCode.equals(post.getProvinceArea()) ||
//                !areaCode.equals(post.getArea()) ||
//                !stateCode.equals(post.getStateArea())) {
//                log.info("更新区域编码字段");
//                postMapper.updateById(post);
//            }
//        }
//
//        // 处理记录
//        boolean result = processSinglePost(post);
//
//        if (result) {
//            log.info("更新岗位区域信息成功: id={}", postId);
//        } else {
//            log.warn("更新岗位区域信息失败: id={}", postId);
//        }
//
//        return result;
//    }
//
//    /**
//     * 处理单条记录
//     */
//    @Override
//    public boolean processSinglePost(Post post) {
//        if (post == null) {
//            log.warn("processSinglePost收到的post对象为null");
//            return false;
//        }
//
//        boolean updated = false;
//
//        try {
//            log.info("正在处理Post记录: id={}, provinceArea={}, area={}, stateArea={}, province={}, city={}, state={}",
//                    post.getId(), post.getProvinceArea(), post.getArea(), post.getStateArea(),
//                    post.getProvince(), post.getCity(), post.getState());
//
//            // 处理省份字段
//            if (StrUtil.isNotBlank(post.getProvinceArea())) {
//                log.info("处理provinceArea: {}", post.getProvinceArea());
//                String provinceInfo = getRegionName(post.getProvinceArea());
//                log.info("查询到的省份信息: {}", provinceInfo);
//
//                if (StrUtil.isNotBlank(provinceInfo)) {
//                    post.setProvince(provinceInfo);
//                    log.info("成功设置province={}", provinceInfo);
//                    updated = true;
//                } else {
//                    log.warn("未找到provinceArea={}的区域名称，未更新province字段", post.getProvinceArea());
//                }
//            } else {
//                log.info("provinceArea为空，跳过省份字段处理");
//            }
//
//            // 处理城市字段（去掉省份前缀）
//            if (StrUtil.isNotBlank(post.getArea())) {
//                log.info("处理area: {}", post.getArea());
//                String cityInfo = getRegionName(post.getArea());
//                log.info("查询到的城市信息: {}", cityInfo);
//
//                if (StrUtil.isNotBlank(cityInfo)) {
//                    // 去掉省份前缀
//                    String province = post.getProvince();
//                    String city = cityInfo;
//                    if (StrUtil.isNotBlank(province) && city.startsWith(province)) {
//                        city = city.substring(province.length());
//                        log.info("去掉省份前缀后的城市: {}", city);
//                    }
//                    post.setCity(city);
//                    log.info("成功设置city={}", city);
//                    updated = true;
//                } else {
//                    log.warn("未找到area={}的区域名称，未更新city字段", post.getArea());
//                }
//            } else {
//                log.info("area为空，跳过城市字段处理");
//            }
//
//            // 处理区县字段（去掉省市前缀）
//            if (StrUtil.isNotBlank(post.getStateArea())) {
//                log.info("处理stateArea: {}", post.getStateArea());
//                String stateInfo = getRegionName(post.getStateArea());
//                log.info("查询到的区县信息: {}", stateInfo);
//
//                if (StrUtil.isNotBlank(stateInfo)) {
//                    // 去掉省市前缀
//                    String province = post.getProvince();
//                    String city = post.getCity();
//                    String state = stateInfo;
//
//                    if (StrUtil.isNotBlank(province) && state.startsWith(province)) {
//                        state = state.substring(province.length());
//                        log.info("去掉省份前缀后的区县: {}", state);
//                    }
//
//                    if (StrUtil.isNotBlank(city) && state.startsWith(city)) {
//                        state = state.substring(city.length());
//                        log.info("去掉城市前缀后的区县: {}", state);
//                    }
//
//                    post.setState(state);
//                    log.info("成功设置state={}", state);
//                    updated = true;
//                } else {
//                    log.warn("未找到stateArea={}的区域名称，未更新state字段", post.getStateArea());
//                }
//            } else {
//                log.info("stateArea为空，跳过区县字段处理");
//            }
//
//            // 如果有更新，则执行保存
//            if (updated) {
//                int rows = postMapper.updateById(post);
//                log.info("更新Post记录成功, 影响行数: {}", rows);
//
//                // 更新后再次查询以验证
//                Post updatedPost = postMapper.selectById(post.getId());
//                if (updatedPost != null) {
//                    log.info("更新后的记录: province={}, city={}, state={}",
//                            updatedPost.getProvince(), updatedPost.getCity(), updatedPost.getState());
//                }
//
//                return rows > 0;
//            } else {
//                log.info("没有字段需要更新");
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("处理Post记录失败, id={}", post.getId(), e);
//            return false;
//        }
//    }
//
//    /**
//     * 根据区域编码获取区域名称
//     */
//    private String getRegionName(String code) {
//        if (StrUtil.isBlank(code)) {
//            log.warn("传入的区域编码为空");
//            return "";
//        }
//
//        log.debug("查询编码 {} 对应的区域名称", code);
//
//        // 检查映射表大小
//        if (regionCodeMap.isEmpty()) {
//            log.warn("区域编码映射表为空，请检查初始化过程");
//            return "";
//        }
//
//        // 取内部中的改造后的code
//        String name = regionCodeMap.get(code);
//        if (StrUtil.isBlank(name)) {
//            // 打印一些键，帮助调试
//            log.warn("未找到编码 {} 的区域名称，当前映射表大小: {}", code, regionCodeMap.size());
//            if (regionCodeMap.size() > 0) {
//                int count = 0;
//                for (String key : regionCodeMap.keySet()) {
//                    if (count < 5) {
//                        log.debug("示例键值对 #{}: {} -> {}", count + 1, key, regionCodeMap.get(key));
//                        count++;
//                    } else {
//                        break;
//                    }
//                }
//            }
//        } else {
//            log.debug("找到编码 {} 对应的区域名称: {}", code, name);
//        }
//        return name != null ? name : "";
//    }
//
//    /**
//     * 用于测试，返回regionCodeMap的大小
//     */
//    public int getRegionCodeMapSize() {
//        return regionCodeMap.size();
//    }
//
//    /**
//     * 用于测试，根据编码获取区域名称
//     */
//    public String testGetRegionName(String code) {
//        return regionCodeMap.getOrDefault(code, "未找到");
//    }
//
//    /**
//     * 内部类：更新结果
//     */
//    private static class UpdateResult {
//        private final int total;
//        private final int success;
//        private final int failed;
//
//        public UpdateResult(int total, int success, int failed) {
//            this.total = total;
//            this.success = success;
//            this.failed = failed;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("总数: %d, 成功: %d, 失败: %d", total, success, failed);
//        }
//    }
//
//    /**
//     * 为方便调试，提供一个解析JSON文本的测试方法
//     * @param jsonText JSON字符串
//     * @return 解析结果
//     */
//    public Map<String, String> testJsonParsing(String jsonText) {
//        Map<String, String> result = new HashMap<>();
//        try {
//            // 将单引号替换为双引号
//            String processedText = jsonText.replace('\'', '"');
//            log.info("处理后的JSON文本: {}", processedText);
//
//            // 解析JSON
//            JSONObject jsonObject = JSONUtil.parseObj(processedText);
//            log.info("解析为JSONObject成功，键数量: {}", jsonObject.size());
//
//            // 转为Map返回
//            for (String key : jsonObject.keySet()) {
//                String value = jsonObject.getStr(key);
//                result.put(key, value);
//            }
//
//            return result;
//        } catch (Exception e) {
//            log.error("JSON解析测试失败: {}", e.getMessage(), e);
//            return result;
//        }
//    }
//}