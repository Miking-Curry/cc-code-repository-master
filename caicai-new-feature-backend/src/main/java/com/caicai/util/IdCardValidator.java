package com.caicai.util;
/**
 * @author YangFuyi
 * @Description: 身份证校验工具
 * @CreateDate: 2025-05-22 10:38
 */
public class IdCardValidator {

    /**
     * 中国公民身份证号码长度
     */
    private static final int ID_CARD_LENGTH_15 = 15;
    private static final int ID_CARD_LENGTH_18 = 18;

    /**
     * 每位加权因子
     */
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 校验码
     */
    private static final char[] VALIDATE_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 有效的地区编码
     * 注意：这里只包含省级编码，实际应用中应当使用完整的行政区划代码表
     * 完整的区划代码可从民政部官网获取并定期更新
     */
    private static final String[] VALID_PROVINCE_CODE = {
            "11", "12", "13", "14", "15", // 华北
            "21", "22", "23", // 东北
            "31", "32", "33", "34", "35", "36", "37", // 华东
            "41", "42", "43", "44", "45", "46", // 华中和华南
            "50", "51", "52", "53", "54", // 西南
            "61", "62", "63", "64", "65", // 西北
            "71", "81", "82" // 港澳台
    };

    /**
     * 校验身份证号码
     *
     * @param idCard 身份证号码
     * @return 校验结果包含是否有效及错误原因
     */
    public static ValidationResult validate(String idCard) {
        if (idCard == null) {
            return new ValidationResult(false, "身份证号码不能为空");
        }

        idCard = idCard.trim();

        if (idCard.isEmpty()) {
            return new ValidationResult(false, "身份证号码不能为空");
        }

        // 转大写处理X字符
        idCard = idCard.toUpperCase();

        // 长度验证
        int length = idCard.length();
        if (length != ID_CARD_LENGTH_15 && length != ID_CARD_LENGTH_18) {
            return new ValidationResult(false, "身份证号码长度错误，应为15位或18位");
        }

        // 15位身份证处理
        if (length == ID_CARD_LENGTH_15) {
            try {
                // 15位身份证升级为18位进行校验
                idCard = convert15To18(idCard);
                if (idCard == null) {
                    return new ValidationResult(false, "15位身份证格式错误");
                }
            } catch (Exception e) {
                return new ValidationResult(false, "15位身份证无效: " + e.getMessage());
            }
        }

        // 基本格式校验
        if (!idCard.matches("^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9X]$")) {
            return new ValidationResult(false, "身份证号码格式错误");
        }

        // 地区码校验
        String provinceCode = idCard.substring(0, 2);
        boolean validProvince = false;
        for (String code : VALID_PROVINCE_CODE) {
            if (code.equals(provinceCode)) {
                validProvince = true;
                break;
            }
        }

        if (!validProvince) {
            return new ValidationResult(false, "身份证地区编码不存在");
        }

        // 出生日期校验
        String birthDateStr = idCard.substring(6, 14);
        try {
            // 检验日期格式
            if (!isValidDate(birthDateStr)) {
                return new ValidationResult(false, "身份证出生日期无效");
            }

            // 检验是否大于当前日期
            if (isFutureDate(birthDateStr)) {
                return new ValidationResult(false, "身份证出生日期不能是未来日期");
            }

        } catch (Exception e) {
            return new ValidationResult(false, "身份证出生日期无效: " + e.getMessage());
        }

        // 校验码校验
        if (!checkValidateCode(idCard)) {
            return new ValidationResult(false, "身份证校验码错误");
        }

        // 全部校验通过
        return new ValidationResult(true, "身份证号码有效");
    }

    /**
     * 简化校验方法，仅返回是否有效
     */
    public static boolean isValid(String idCard) {
        return validate(idCard).isValid();
    }

    /**
     * 检查校验码是否正确
     */
    private static boolean checkValidateCode(String idCard) {
        char[] chars = idCard.toCharArray();
        int sum = 0;

        for (int i = 0; i < 17; i++) {
            // 字符'0'的ASCII码是48
            sum += (chars[i] - '0') * WEIGHT[i];
        }

        int modValue = sum % 11;
        char calcCode = VALIDATE_CODE[modValue];
        char lastChar = chars[17];

        return lastChar == calcCode;
    }

    /**
     * 15位身份证号码升级为18位
     *
     * 15位：RRRRRRYYMMDDIII
     * 18位：RRRRRRYYYYYMMDDSSSC
     * 其中
     * R 为行政区划代码
     * Y 为年份
     * M 为月份
     * D 为日期
     * I 为个人顺序码
     * S 为顺序码
     * C 为校验码
     */
    private static String convert15To18(String idCard15) {
        // 15位身份证基本格式校验
        if (!idCard15.matches("^[1-9]\\d{14}$")) {
            return null;
        }

        StringBuilder idCard18 = new StringBuilder();

        // 复制地区码
        idCard18.append(idCard15.substring(0, 6));

        // 扩展年份,15位身份证年份是2位,表示19XX年,如19代表1919,添加19前缀
        idCard18.append("19");

        // 复制年月日和顺序码
        idCard18.append(idCard15.substring(6));

        // 计算校验码
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard18.charAt(i) - '0') * WEIGHT[i];
        }

        // 添加校验码
        idCard18.append(VALIDATE_CODE[sum % 11]);

        return idCard18.toString();
    }

    /**
     * 判断输入的日期字符串是否是有效日期
     * 身份证日期格式为yyyyMMdd
     */
    private static boolean isValidDate(String dateStr) {
        try {
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6));

            // 年月日合理性校验
            if (year < 1900 || month <= 0 || month > 12 || day <= 0 || day > 31) {
                return false;
            }

            // 判断日期是否有效
            if (month == 2) {  // 闰年2月处理
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if (day > (isLeapYear ? 29 : 28)) {
                    return false;
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否是未来日期
     */
    private static boolean isFutureDate(String dateStr) {
        try {
            int currentYear = java.time.LocalDate.now().getYear();
            int currentMonth = java.time.LocalDate.now().getMonthValue();
            int currentDay = java.time.LocalDate.now().getDayOfMonth();

            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6));

            if (year > currentYear) {
                return true;
            } else if (year == currentYear) {
                if (month > currentMonth) {
                    return true;
                } else if (month == currentMonth) {
                    return day > currentDay;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验结果封装类
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "ValidationResult{valid=" + valid + ", message='" + message + "'}";
        }
    }

    /**
     * 使用示例
     */
    public static void main(String[] args) {
        // 1. 基本校验示例
        String[] testIds = {
                "110101199003070857",  // 假设有效的身份证号
                "11010119900307085X",  // 带X的身份证号
                "110101990307085",     // 15位身份证号
                "123456789012345",     // 无效的15位身份证
                "12345678901234567",   // 无效的18位身份证
                "110101199013070857",  // 月份错误
                "110101199003320857",  // 日期错误
                "110101299003070857",  // 未来日期
        };

        for (String id : testIds) {
            ValidationResult result = validate(id);
            System.out.println("身份证: " + id + " -> " + result);
        }

        // 2. 批量校验示例
        System.out.println("\n批量校验示例:");
        java.util.List<String> idList = java.util.Arrays.asList(
                "110101199003070857", "11010119900307085X"
        );

        for (String id : idList) {
            if (isValid(id)) {
                System.out.println("身份证: " + id + " -> 有效");
            } else {
                System.out.println("身份证: " + id + " -> 无效");
            }
        }
    }
}
