package com.caicai.enums;

import lombok.Getter;

/**
 * 岗位状态枚举类
 * 1: 急聘  2: 新岗  3: 推荐  4: 优选  5: 普通
 */
@Getter
public enum PostStatusType {
    
    URGENT(1, "急"),
    NEW(2, "新"),
    RECOMMENDED(3, "荐"),
    PREMIUM(4, "优"),
    NORMAL(5, "普");
    
    private final Integer code;
    private final String desc;
    
    PostStatusType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据状态码获取对应的枚举值
     * 
     * @param code 状态码
     * @return 对应的枚举值，如果没有找到则返回null
     */
    public static PostStatusType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (PostStatusType status : PostStatusType.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        
        return null;
    }
    
    /**
     * 根据状态码获取对应的描述
     * 
     * @param code 状态码
     * @return 对应的描述，如果没有找到则返回"未知状态"
     */
    public static String getDescByCode(Integer code) {
        PostStatusType status = getByCode(code);
        return status == null ? "未知状态" : status.getDesc();
    }
}
