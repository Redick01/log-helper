package com.ruubypay.log;

import com.ruubypay.log.annotation.FieldIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liupenghui
 * @date 2021/11/9 5:47 下午
 */
@Data
@AllArgsConstructor
public class UserInfo {

    private String name;

    private int age;

    @FieldIgnore
    private String phoneNo;
}
