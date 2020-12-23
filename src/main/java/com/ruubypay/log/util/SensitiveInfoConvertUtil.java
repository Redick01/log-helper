package com.ruubypay.log.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 日志打印内容敏感信息处理
 * @Author liu_penghui
 * @Date 2018/10/19.
 */
public class SensitiveInfoConvertUtil {

    /**
     * [姓名] 只显示第一个汉字，其他隐藏为星号<例子：李**>
     *
     * @param fullName
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName.replaceAll("'", ""), 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName.replaceAll("'", "")), "*");
    }
    /**
     * [身份证号] 显示前三位最后四位，其他隐藏。共计18位或者15位。<例子：123**********5762>
     *
     * @param idCardNum
     * @return
     */
    public static String idCardNum(String idCardNum) {
        if (StringUtils.isBlank(idCardNum)) {
            return "";
        }
        return idCardNum.replaceAll("(?<=\\w{3})\\w(?=\\w{2})", "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return num.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");    }
    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * macKey脱敏
     * @param mac
     * @return
     */
    public static String macKey(String mac) {
        if (StringUtils.isEmpty(mac)) {
            return "";
        }
        return mac.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }
}
