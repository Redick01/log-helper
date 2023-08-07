/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 日志打印内容敏感信息处理
 * @author liu_penghui
 *  2018/10/19.
 */
@SuppressWarnings("all")
public class SensitiveInfoConvertUtil {

    private static final int START = 6;

    private static final int END = 4;

    /**
     * 中文名脱敏
     * @param fullName real name
     * @return 脱敏后数据
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName.replaceAll("'", ""), 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName.replaceAll("'", "")), "*");
    }
    /**
     * 显示前三位最后四位，其他隐藏。共计18位或者15位
     *
     * @param idCardNum 身份证
     * @return 脱敏后数据
     */
    public static String idCardNum(String idCardNum) {
        if (StringUtils.isBlank(idCardNum)) {
            return "";
        }
        return idCardNum.replaceAll("(?<=\\w{3})\\w(?=\\w{2})", "*");
    }

    /**
     * 前三位，后四位，其他隐藏
     *
     * @param num 电话号码
     * @return 脱敏后数据
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return num.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    /**
     * 前六位，后四位，其他用星号隐藏每位1个星号
     *
     * @param cardNum 银行卡号
     * @return 脱敏后数据
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, START)
                .concat(StringUtils
                        .removeStart(StringUtils
                                .leftPad(StringUtils
                                        .right(cardNum, END),
                                        StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * macKey脱敏
     * @param mac 鉴权值
     * @return 脱敏后数据
     */
    public static String macKey(String mac) {
        if (StringUtils.isEmpty(mac)) {
            return "";
        }
        return mac.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }
}
