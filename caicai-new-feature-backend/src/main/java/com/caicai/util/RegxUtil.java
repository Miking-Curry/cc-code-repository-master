package com.caicai.util;
//用于校验手机号
public class RegxUtil {
    public static boolean checkPhone(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        return phone.matches(regex);
    }
    //用于校验名字
    public static boolean checkName(String name) {
        String regex = "^[\\u4e00-\\u9fa5]{2,4}$";
        return name.matches(regex);

    }
}
