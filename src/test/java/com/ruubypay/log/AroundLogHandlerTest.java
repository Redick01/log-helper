package com.ruubypay.log;

import com.ruubypay.miss.dto.facade.request.CommonRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

/**
 * @author liupenghui
 * @date 2021/11/9 5:39 下午
 */
@RunWith(MockitoJUnitRunner.class)
public class AroundLogHandlerTest {

    private CommonRequest<UserInfo> commonRequest;

    @Before
    public void startup() {
        commonRequest = new CommonRequest<>();
        commonRequest.setUserId(111);
        UserInfo info = new UserInfo("redick", 30, "17777786993");
        commonRequest.setReqData(info);
    }

    @Test
    public void getBeginParameterTest() {
        HashMap<String, Object> map = AroundLogHandler.getParamHashMapForObject(commonRequest);
        map.forEach((k, v) -> System.out.println("key：" + k + "--" + "value：" + v));
    }
}
