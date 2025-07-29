package com.caicai.enums;

public enum EducationType {
    PRIMARY_SCHOOL(1 ,"小学"),
    JUNIOR_HIGH_SCHOOL(2 ,"初中"),
    SECONDARY_SCHOOL(3 ,"中专"),
    HIGH_SCHOOL(4 ,"高中"),
    COLLEGE(5 ,"大专"),
    UNDERGRADUATE(6 ,"本科"),
    MASTER(7 ,"硕士"),
    DOCTOR(8 ,"博士");

    private final Integer code;
    private final String desc;

    EducationType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (EducationType education : EducationType.values()) {
            if (education.getCode().equals(code)) {
                return education.getDesc();
            }
        }
        return "";
    }

}
