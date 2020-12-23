package com.ruubypay.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.ruubypay.log.common.SensitiveType;
import com.ruubypay.log.util.SensitiveInfoConvertUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏信息处理
 * @Author liu_penghui
 * @Date 2018/10/22.
 */
public class SensitiveDataConverter extends MessageConverter {

    /**
     * 日志脱敏开关
     */
    private static boolean converterCanRun = true;
    /**
     * 日志脱敏关键字
     */
    private static String sensitiveDataKeys = "idCard,realName,bankCard,mobile,mac,macKey";
    /**
     * 脱敏正则
     */
    private static Pattern pattern = Pattern.compile("[0-9a-zA-Z]");

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始日志
        String oriLogMsg = event.getFormattedMessage();

        // 获取脱敏后的日志
        String afterLogMsg = invokeMsg(oriLogMsg);
        return afterLogMsg;
    }

    /**
     * 处理日志字符串，返回脱敏后的字符串
     * @param oriLogMsg
     * @return
     */
    public static String invokeMsg(final String oriLogMsg) {
        String tempMsg = oriLogMsg;
        if(converterCanRun){
            // 处理字符串
            if(sensitiveDataKeys != null && sensitiveDataKeys.length() > 0){
                String[] keysArray = sensitiveDataKeys.split(",");
                for(String key: keysArray){
                    int index= -1;
                    do{
                        index = tempMsg.indexOf(key, index+1);
                        if(index != -1){
                            // 判断key是否为单词字符
                            if(isWordChar(tempMsg, key, index)){
                                continue;
                            }
                            // 寻找值的开始位置
                            int valueStart = getValueStartIndex(tempMsg, index + key.length());

                            // 查找值的结束位置（逗号，分号）........................
                            int valueEnd = getValuEndEIndex(tempMsg, valueStart);

                            // 对获取的值进行脱敏
                            String subStr = tempMsg.substring(valueStart, valueEnd);
                            subStr = sensitiveConvert(subStr, key);
                            ///////////////////////////
                            tempMsg = tempMsg.substring(0,valueStart) + subStr + tempMsg.substring(valueEnd);
                        }
                    }while(index != -1);
                }
            }
        }
        return tempMsg;
    }

    /**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     * @param msg
     * @param key
     * @param index
     * @return
     */
    private static boolean isWordChar(String msg, String key, int index){

        // 必须确定key是一个单词............................
        // 判断key前面一个字符
        if(index != 0){
            char preCh = msg.charAt(index-1);
            Matcher match = pattern.matcher(preCh + "");
            if(match.matches()){
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = pattern.matcher(nextCh + "");
        if(match.matches()){
            return true;
        }
        return false;
    }

    /**
     * 获取value值的开始位置
     * @param msg 要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private static int getValueStartIndex(String msg, int valueStart ){
        // 寻找值的开始位置.................................
        // key与 value的分隔符
        do{
            char ch = msg.charAt(valueStart);
            if(ch == ':' || ch == '='){
                valueStart ++;
                ch = msg.charAt(valueStart);
                if(ch == '"'){
                    valueStart ++;
                }
                break;    // 找到值的开始位置
            }else{
                valueStart ++;
            }
        }while(true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     * @return
     */
    private static int getValuEndEIndex(String msg,int valueEnd) {
        do{
            if(valueEnd == msg.length()){
                break;
            }
            char ch = msg.charAt(valueEnd);
            // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
            if(ch == '"'){
                if(valueEnd+1 == msg.length()){
                    break;
                }
                char nextCh = msg.charAt(valueEnd+1);
                if(nextCh ==';' || nextCh == ','){
                    // 去掉前面的 \  处理这种形式的数据
                    while(valueEnd>0 ){
                        char preCh = msg.charAt(valueEnd-1);
                        if(preCh != '\\'){
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                }else{
                    valueEnd ++;
                }
            }else if (ch ==';' || ch == ',' || ch == '}'){
                break;
            }else{
                valueEnd ++;
            }

        }while(true);
        return valueEnd;
    }

    /**
     * 脱敏
     * @param submsg
     * @param key
     * @return
     */
    public static String sensitiveConvert(String submsg, String key){
        // idCard：身份证号, realName：姓名, bankCard：银行卡号, mobile：手机号, mac/macKey：密钥
        if (SensitiveType.ID_CARD.equals(key)) {
            return SensitiveInfoConvertUtil.idCardNum(submsg);
        }
        if (SensitiveType.REAL_NAME.equals(key)) {
            return SensitiveInfoConvertUtil.chineseName(submsg);
        }
        if (SensitiveType.BANK_CARD.equals(key)) {
            return SensitiveInfoConvertUtil.bankCard(submsg);
        }
        if (SensitiveType.MOBILE.equals(key)) {
            return SensitiveInfoConvertUtil.mobilePhone(submsg);
        }
        if (SensitiveType.MAC.equals(key) || SensitiveType.MAC_KEY.equals(key)) {
            return SensitiveInfoConvertUtil.macKey(submsg);
        }
        return "";
    }
}
