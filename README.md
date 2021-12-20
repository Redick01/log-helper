# 日志工具集成

## 1 支持内容

3.0.0-RELEASE版本

- 日志json格式打印
- 统一切面，提供切面注解打印切面入口输入参数和输出参数以及执行时间
- 支持以MDC的方式打印traceId以及切面业务描述
- 支持java bean，集合类型，HttpServletRequest等参数类型的日志打印
- 异步线程日志链路追踪，支持java线程池和spring线程池的异步日志链路追踪
- 支持Alibaba Dubbo和Apache Dubbo分布式日志链路追踪
- 支持Spring Cloud OpenFeign分布式日志链路追踪
- 提供HttpClient，OkHttp，RestTemplate日志链路追踪解决方案
- 提供Apache RocketMQ，Aliyun RocketMQ日志链路追踪解决方案
- 支持Spring Web日志链路追踪处理
- 支持以SkyWalking traceId作为日志traceId
- 提供Spring命名空间和SpringBoot两种接入方式
- 提供简单的字段脱敏解决方案


## 2 快速开始

### 2.1 logback.xml配置

由于该日志工具集成了logstash，用于将日志格式化成json，所以在logback配置文件中指定日志格式配置是先决条件，配置如下：

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
            <includeMdcKeyName>traceId</includeMdcKeyName>
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
            <includeMdcKeyName>traceId</includeMdcKeyName>
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
            <includeMdcKeyName>traceId</includeMdcKeyName>
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

### 2.2 SpringBoot程序接入

- **pom依赖**

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-spring-boot-starter</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```

- **应用程序启动开启日志自动装配**

在程序启动类上添加`@LogHelperEnable`注解即可完成自动装配

```java
@SpringBootApplication
@LogHelperEnable
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
```

- **使用方式**

使用`@LogMarker`注解标注切面，示例如下：

```java
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/sc-tcc/submitOrder")
    @LogMarker(businessDescription = "提交订单", interfaceName = "/sc-tcc/submitOrder")
    public String submitOrder(@RequestBody SubmitOrderDTO dto) {
        String orderNo = UUID.randomUUID().toString();
        try {
            orderService.saveOrder(new StockDTO(orderNo, dto.getProductId(), dto.getPayCount(), UUID.randomUUID().toString()));
            return "SUCCESS";
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "异常", e);
            return "ERROR";
        }
    }
}
```

日志内容：

```json
{
    "@timestamp":"2021-12-20T17:45:05.101+08:00",
    "@version":"1",
    "message":"开始处理",
    "logger_name":"org.transaction.tcc.order.controller.OrderController",
    "thread_name":"http-nio-9109-exec-2",
    "level":"INFO",
    "level_value":20000,
    "request_type":"提交订单",
    "x_global_session_id":"82e56e5a-ea27-4cf5-bf49-e5ae9965f65c",
    "log_pos":"开始处理",
    "data":{
        "productId":1,
        "payCount":100
    }
}
.....
{
    "@timestamp":"2021-12-20T17:45:06.522+08:00",
    "@version":"1",
    "message":"处理完毕",
    "logger_name":"org.transaction.tcc.order.controller.OrderController",
    "thread_name":"http-nio-9109-exec-2",
    "level":"INFO",
    "level_value":20000,
    "request_type":"提交订单",
    "x_global_session_id":"82e56e5a-ea27-4cf5-bf49-e5ae9965f65c",
    "log_pos":"处理完毕",
    "data":{
        "serialPersistentFields":[

        ],
        "CASE_INSENSITIVE_ORDER":{

        },
        "serialVersionUID":-6849794470754667710,
        "value":"SUCCESS",
        "hash":0
    },
    "duration":1410,
    "result":"成功"
}
```


### 2.3 Spring Namespce接入

- **pom**

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-core</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-spring</artifactId>
    <version>2.0.7-RELEASE</version>
</dependency>
```



- **Spring AOP配置**

配置aop：用于打印通用的日志，如，接口调用时间、请求参数、返回参数、全局会话ID、request_type，使用该日志插件不需要自己打请求参数和返回值，插件会自动打印.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:logmarker="http://www.redick.com/schema/logmarker"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
http://www.redick.com/schema/logmarker http://www.redick.com/schema/logmarker/logmarker.xsd">
    <!--日志模版aop，处理@LogMarker注解标注的方法-->
    <logmarker:handler/>
    <logmarker:interceptor/>
    <aop:aspectj-autoproxy proxy-target-class="true"/>
    <aop:config proxy-target-class="true">
        <aop:aspect ref="logMarkerInterceptor">
            <aop:pointcut id="logCut"
                          expression="execution(* com.XXX.XXX.XXX.*.*(..)) &amp;&amp;@annotation(com.redick.annotation.LogMarker)"/>
            <aop:around pointcut-ref="logCut" method="proceed"/>
        </aop:aspect>
    </aop:config>
</beans>
```

- **使用方式**

同SpringBoot接入方式

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

### 2.4 总结

基础的接入方式就这么多，下面了解下一些特殊场景支持的接入方式

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 3 异步线程链路追踪支持

### 3.1 SpringBoot接入

**SpringBoot通过自动装配已经支持，无需多余配置。**


### 3.2 Spring Namespace接入

**非SpringBoot程序配置支持异步线程链路追踪，需要在程序启动时加载一个MDCAdapter，建议使用@PostConstruct的方式，代码如下：**

```java
@PostConstruct
public void init() throws Exception {
    TtlMDCAdapter.getInstance();
}
```

### 3.3 使用线程支持异步线程链路追踪

**使用线程池的程序想要支持链路追踪，不仅需要增强`MDCAdapter`还需要对线程池进行一定的修饰，使用的是`TransmittableThreadLocal`的API进行的线程池修饰；日志工具包提供了线程池修饰的实现，如下：**

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

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 4 SpringCloud OpenFeign支持

### 4.1 SpringBoot接入

**SpringBoot通过自动装配已经支持，无需多余配置。**

### 4.2 Spring Namespace接入

**RPC调用使用OpenFeign需要进行以下配置：**

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

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 5 Apache Dubbo支持和Alibaba Dubbo支持

### 5.1 Apache Dubbo支持

无论SpringBoot还是传统Spring Namespace只要需要引入依赖即可：

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-apachedubbo</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```

### 5.2 Alibaba Dubbo支持

类似`5.1 Apache Dubbo支持`

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-alibabadubbo</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 6 MQ消息队列解决方案

无论SpringBoot还是传统Spring Namespace只要需要引入依赖即可：

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-mq</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```

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
 
&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 7 HttpClient支持

工具包支持HttpClient4和HttpClient5，HttpClient支持traceId需要代码入侵，具体实现方案是对HttpClient添加拦截器，拦截器的作用是将traceId放到Http Header中。

无论SpringBoot还是传统Spring Namespace只要需要引入依赖即可：

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-httpclient</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```



### 7.1 HttpClient4示例代码：

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

### 7.2 HttpClient5示例代码：

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

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp; 

## 8 OkHttp支持

 工具包提供OkHttp客户端支持，实现方法与HttpClient类似，均以拦截器形式实现

 无论SpringBoot还是传统Spring Namespace只要需要引入依赖即可：

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-httpclient</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```
 
### 8.1 OkHttp示例代码：

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

### 8.2 OkHttp3示例代码：

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

&nbsp; &nbsp; 
&nbsp; &nbsp; 
&nbsp; &nbsp;

## 9 Spring Web RestTemplate支持

 无论SpringBoot还是传统Spring Namespace只要需要引入依赖即可：

```xml
<dependency>
    <groupId>com.redick</groupId>
    <artifactId>log-helper-httpclient</artifactId>
    <version>3.0.0-RELEASE</version>
</dependency>
```

 工具包提供了，RestTemplate支持，实现方案也是以拦截器的方式，只需要在使用RestTemplate时添加如下代码即可；
 
```java
restTemplate.setInterceptors(Collections.singletonList(new SessionIdRestTemplateInterceptor()));
```

## 10 日志打印自定义操作及建议规范

参考：[日志打印自定义操作及建议规范](/use-detail.md)