package com.caicai.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName: TaskActionType
 * @Description: 任务行为类型
 * @Author: PCT
 * @Date: 2025/6/6 9:52
 * @Version: 1.0
 */

@Getter
public enum TaskActionTypeEnums {
    SIGN_IN(1, "SIGN_IN", "每日签到", "用户每日点击签到按钮"),
    REFINE_PROFILE(2, "REFINE_PROFILE", "完善信息", "补全手机号、邮箱、头像等个人信息"),
    REFINE_RESUME(3, "REFINE_RESUME", "完善简历", "用户完善简历信息(上传PDF或填写简历内容)"),
    BIND_WECHAT(4, "BIND_WECHAT", "绑定微信", "用户绑定微信账号"),
    VIEW_POST(5, "VIEW_POST", "浏览职位", "浏览某个职位详情页5秒以上"),
    SEARCH_POST(6, "SEARCH_POST", "搜索职位", "发起一次职位搜索行为"),
    DELIVER_POST(7, "DELIVER_POST", "投递职位", "向职位投递简历"),
    FAVOURITE_POST(8, "FAVOURITE_POST", "收藏职位", "收藏一个职位"),
    ATTEND_INTERVIEW(9, "ATTEND_INTERVIEW", "参加面试", "面试状态变为“已参加”"),
    INVITE_USER(10, "INVITE_USER", "邀请好友注册", "发出邀请并成功注册"),
    SHARE_POST(11, "SHARE_POST", "分享职位", "分享职位信息链接至好友/朋友圈并被打开");

    private final Integer id;
    private final String code;
    private final String name;
    private final String desc;

    TaskActionTypeEnums(Integer id, String code, String name, String desc) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public static TaskActionTypeEnums ofId(Integer id) {
        return Arrays.stream(values())
                .filter(e -> e.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static TaskActionTypeEnums ofCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
