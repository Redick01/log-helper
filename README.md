# 日志工具集成

- **1.0.X-RELEASE**

1. 支持参数脱敏
2. 支持接口请求/响应参数统一打印，支持接口执行时间计算
3. 支持java object参数，支持Java集合参数，支持Servlet的HttpServletRequest参数
4. 支持Alibaba Dubbo的调用链路追踪

- **2.0.X-RELEASE**

1. 修复接口参数列表多个，处理HttpServletRequest参数错误导致OOM的BUG
2. 支持Apache Dubbo的调用链路追踪
3. 支持异步线程的调用链路追踪
4. 提供基于SpringMVC的Http接口调用链路追踪方案

- **2.0.6-RELEASE**

1. 支持SpringCloud框架下，Openfeign组件链路支持
2. 支持Skywalking traceId作为日志x-global-sessionid打印日志

## SpringBoot程序集成

参考：http://gitlab.ruubypay.net/MISS-2.0/BasicPlatform/middleware/ruubypay-log-spring-boot-starter


## 传统Spring程序集成

- **pom**

```xml
        <!--日志模版依赖-->
        <dependency>
            <groupId>com.ruubypay.miss</groupId>
            <artifactId>ruubypay-log-helper</artifactId>
            <version>2.6-RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.9</version>
        </dependency>
```
     
- **logback修改：**

更多详细参考[详细使用文档](/use-detail.md)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="logs/" />
    <property name="FILE_NAME" value="MISS.RBPS_Service" />
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <!--json格式化输出日志-->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>x_global_session_id</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
        </encoder>
    </appender>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/${FILE_NAME}.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <!-- 文件扩展名设置为.zip/.gz后在文件滚动时会自动对旧日志进行压缩 -->
            <FileNamePattern>${LOG_HOME}/${FILE_NAME}.log.%d{yyyyMMdd}.%i.zip</FileNamePattern>
            <maxFileSize>1GB</maxFileSize>
            <totalSizeCap>40GB</totalSizeCap>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <!--json格式化输出日志-->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>x_global_session_id</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
        </encoder>
        <prudent>false</prudent>
    </appender>

    <appender name="FILE_DEBUG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/debug/${FILE_NAME}_debug.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <!-- 文件扩展名设置为.zip/.gz后在文件滚动时会自动对旧日志进行压缩 -->
            <FileNamePattern>${LOG_HOME}/debug/${FILE_NAME}_debug.log.%d{yyyyMMdd}.%i.zip</FileNamePattern>
            <maxFileSize>1GB</maxFileSize>
            <totalSizeCap>40GB</totalSizeCap>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <!--json格式化输出日志-->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>x_global_session_id</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
        </encoder>
        <prudent>false</prudent>
    </appender>
    <!-- 日志输出级别 -->
    <logger name="io.netty" level="WARN" />
    <root>
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="FILE_DEBUG"/>
    </root>
</configuration>
```


- **Spring AOP配置**

配置aop：用于打印通用的日志，如，接口调用时间、请求参数、返回参数、全局会话ID、request_type，使用该日志插件不需要自己打请求参数和返回值，插件会自动打印.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:logmarker="http://www.ruubypay.com/schema/logmarker"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
http://www.ruubypay.com/schema/logmarker http://www.ruubypay.com/schema/logmarker/logmarker.xsd">
    <!--日志模版aop，处理com.ruubypay.service.impl包下的类中@LogMarker注解标注的方法-->
    <logmarker:handler />
    <logmarker:interceptor/>
    <aop:aspectj-autoproxy proxy-target-class="true"/>
    <aop:config proxy-target-class="true">
        <aop:aspect ref="logMarkerInterceptor">
            <aop:pointcut id="logCut"
                          expression="execution(* com.ruubypay.service.impl.*.*(..)) &amp;&amp;@annotation(com.ruubypay.log.annotation.LogMarker)"/>
            <aop:around pointcut-ref="logCut" method="proceed"/>
        </aop:aspect>
    </aop:config>
</beans>
```

-  **在任意要拦截的接口添加@LogMarker注解**

一般都是在controller或者rpc调用的入口处统计。

```java
    @LogMarker(businessDescription = "用户注册的接口")
    @RequestMapping(value = "/user/user1")
    public CommonResponse getBy(@RequestBody @Valid CommonRequest<User> user){
        return new CommonResponse<>("ok","成功");
    }  
```

## 异步线程链路追踪支持

- **非SpringBoot程序配置支持异步线程链路追踪，需要在程序启动时加载一个MDCAdapter，建议使用@PostConstruct的方式，代码如下：**

```java
@PostConstruct
public void init() throws Exception {
    TtlMDCAdapter.getInstance();
}
```

- **使用线程支持异步线程链路追踪**

使用线程池的程序想要支持链路追踪，不仅需要增强`MDCAdapter`还需要对线程池进行一定的修饰，使用的是`TransmittableThreadLocal`的API进行的线程池修饰；日志工具包提供了线程池修饰的实现，如下：

1. 对JUC线程池ThreadPoolExecutor的修饰类是：TtlThreadPoolExecutor
2. 对Spring的ThreadPoolTaskExecutor的修饰类是：TtlThreadPoolTaskExecutor

使用实例如下：

```java
    public static void main(String[] args) {
        TtlThreadPoolTaskExecutor paymentThreadPool = new TtlThreadPoolTaskExecutor();
        paymentThreadPool.setCorePoolSize(5);
        paymentThreadPool.setMaxPoolSize(10);
        paymentThreadPool.setKeepAliveSeconds(60);
        paymentThreadPool.setQueueCapacity(1000);
        paymentThreadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("trans-dispose-%d").build());
        paymentThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        paymentThreadPool.initialize();
        paymentThreadPool.execute(() -> {
            System.out.println("异步线程执行");
        });
    }
```

## SpringCloud OpenFeign支持

### SpringBoot程序直接使用Starter即可

### 非SpringBoot程序配置

RPC调用使用OpenFeign需要进行以下配置：

- **Producer端配置，使用java配置或XML配置均可**

 java配置

```java
@Configuration
@ConditionalOnClass(RequestTemplate.class)
public class FeignFilterConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "feignRequestFilter")
    public FeignRequestFilter feignRequestFilter() {
        return new FeignRequestFilter();
    }
}
```

 或XML配置

```xml
<bean class="com.ruubypay.log.filter.feign.FeignRequestFilter"/>
```

- **Consumer端配置**

```java
@Configuration
@ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
public class WebInterceptorConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "webConfiguration")
    public WebConfiguration webConfiguration() {
        return new WebConfiguration();
    }
}
```

或XML配置

```xml
<bean class="com.ruubypay.log.filter.web.WebConfiguration"/>
```

## MQ消息队列支持

 对MQ消息队列的支持需要对应用程序的业务代码入侵，方案是对业务的Bean进行装饰，日志工具包提供了一个MqWrapperBean用于包装业务Bean，具体使用代码如下：

- **Producer端**

```java
    @Override
    public void submitOrder(Long productId, Integer payCount) {
        // 发送事务消息
        TxMessage txMessage = new TxMessage();
        // 全局事务编号
        String txNo = UUID.randomUUID().toString();
        txMessage.setProductId(productId);
        txMessage.setPayCount(payCount);
        txMessage.setTxNo(txNo);
        txMessage.setOrderNo(UUID.randomUUID().toString());
        MqWrapperBean<TxMessage> mqWrapperBean = new MqWrapperBean<>(txMessage);
        String jsonString = JSONObject.toJSONString(mqWrapperBean);
        Message<String> msg = MessageBuilder.withPayload(jsonString).build();
        rocketMQTemplate.sendMessageInTransaction("tx_order_group", "topic_txmsg", msg, null);
    }
```

- **Consumer端**

 示例使用的是RocketMq的事务消息，MqConsumer接口提供了消费事务消息和普通消息的方法，业务代码自己实现消费业务数据。非事务消息使用`consume`，事务消息使用`localTransactionConsume`

```java
            MqConsumerProcessor.processLocalTransaction(getMqWrapperBean(message), new MqConsumer<TxMessage>() {
                @Override
                public void consume(TxMessage o) {

                }

                @Override
                public RocketMQLocalTransactionState localTransactionConsume(TxMessage txMessage) {
                    orderService.submitOrderAndSaveTxNo(txMessage);
                    // 返回commit
                    return RocketMQLocalTransactionState.COMMIT;
                }
            });
```

 工具包提供了阿里云RocketMq的消费这支持`AliyunMqConsumer`
 
## HttpClient支持

 工具包支持HttpClient4和HttpClient5，HttpClient支持x-global-session-id需要代码入侵，具体实现方案是对HttpClient添加拦截器，拦截器的作用是将traceId放到Http Header中。

HttpClient4示例代码：

```java
public class HttpClientExample {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        CloseableHttpClient client = HttpClientBuilder.create()
                .addInterceptorFirst(new SessionIdHttpClientInterceptor())
                .build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity  = response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

HttpClient5示例代码：

```java
public class HttpClient5Example {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        CloseableHttpClient client = HttpClientBuilder.create()
                .addRequestInterceptorFirst(new SessionIdHttpClient5Interceptor())
                .build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity  = response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## OkHttp支持

 工具包提供OkHttp客户端支持，实现方法与HttpClient类似，均以拦截器形式实现
 
OkHttp示例代码：

```java
public class OkHttpExample {

    public static void main(String[] args) throws IOException {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new SessionIdOkhttpInterceptor());
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
    }
}
```

OkHttp3示例代码：

```java
public class OkHttp3Example {

    public static void main(String[] args) throws IOException {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new SessionIdOkhttp3Interceptor())
                .build();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
    }
}
```

## Spring Web RestTemplate支持

 工具包提供了，RestTemplate支持，实现方案也是以拦截器的方式，只需要在使用RestTemplate时添加如下代码即可；
 
```java
restTemplate.setInterceptors(Collections.singletonList(new SessionIdRestTemplateInterceptor()));
```