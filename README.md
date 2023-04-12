# 基于Spring AOP + logstash-logback-encoder日志链路追踪工具LogHelper

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

&nbsp; &nbsp; **🔥🔥🔥轻量级日志链路追踪工具，结合logstash-logback-encoder实现日志输出json格式化；支持Sykwalking traceId，支持Apache Dubbo，Alibaba Dubbo，SpringCloud微服务框架日志链路追踪；支持异步线程日志链路追踪；支持OkHttp，HttpClient，RestTemplate Http客户端工具日志链路追踪；提供分布式消息队列日志链路追中解决方案；支持简单的敏感字段脱敏打印**

**项目地址：**[https://github.com/Redick01/log-helper](https://github.com/Redick01/log-helper)  欢迎交流👏🏻

**项目目前使用规模：**服务数`200+` 服务器实例`300+`

## 背景

###### &nbsp; &nbsp; 公司的日志系统ELK（ElasticSearch、Logstash、Kibana）搭建的，其架构是Filebeat收集日志传输到Logstash，Logstash解析日志然后将日志存储到Elasticsearch中，最后通过Kibana查询展示日志，最初公司各个系统没有对日志的打印形成规范，导致了日志打印的形式各种各样，这无疑使得Logstash解析日志的配置变得十分复杂，并且一开始公司的日志也没有链路追踪的能力，所以为了解决如下问题决定开发一个日志标准化工具。待解决问题如下：

- **1.日志内容格式不统一，ELK系统解析日志麻烦**
- **2.单体服务接口日志链路踪能力缺失**
- **3.微服务RPC中间件跨进程日志链路追踪能力缺失**
- **4.无法统一做到接口传递参数脱敏**
- **5.分布式消息队列链路追踪能力缺失**
- **6.异步线程，线程池链路追踪能力缺失**
- **7.无法配合APM工具（Skywalking）生成的traceId作为日志链路追踪的traceId**

## 1 支持内容

##### 1.0-RELEASE版本

- **日志json格式打印**
- **统一切面，提供切面注解打印切面入口输入参数和输出参数以及执行时间**
- **支持以MDC的方式打印traceId以及切面业务描述**
- **支持java bean，集合类型，HttpServletRequest等参数类型的日志打印**
- **异步线程日志链路追踪，支持java线程池和spring线程池的异步日志链路追踪**
- **支持Alibaba Dubbo和Apache Dubbo中间件日志链路追踪**
- **支持Spring Cloud OpenFeign中间件日志链路追踪**
- **支持HttpClient，OkHttp，RestTemplate客户端日志链路追踪**
- **提供Apache RocketMQ，Aliyun RocketMQ日志链路追踪解决方案**
- **支持以SkyWalking traceId作为服务日志traceId**
- **提供简单的字段脱敏解决方案**
- **提供接口参数解析接口，支持自定义接口参数的解析，只需要按SPI规范实现即可**
- **提供Spring命名空间和SpringBoot两种接入方式**

##### 1.0.1-RELEASE版本

- **支持forest框架日志链路追踪**
- **支持hutool httpclient框架日志链路追踪**
- **支持spring webflux日志链路追踪**

##### 1.0.2-RELEASE版本

- **支持简单的链路父子关系，即span、parent**

##### 1.0.3-RELEASE版本

- **支持简单mysql执行时间统计**
- **支持motan RPC框架日志链路追踪**

##### 1.0.3-RELEASE版本

- **支持简单mysql执行时间统计**
- **支持motan RPC框架日志链路追踪**

###### 1.0.4-RELEASE版本

- **支持apache dubbo,alibaba dubbo,motan,grpc调用耗时计算**
- **支持forest,httpclient,httpclient5,okhttp,okhttp3,restTemplate调用耗时计算**
- **支持自定义标签，参考LogUtil#customizeMarker**

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

## 2 快速开始

## 2.1 SpringBoot接入

##### **pom依赖**

```xml
<dependency>
    <groupId>io.github.redick01</groupId>
    <artifactId>log-helper-spring-boot-starter-common</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

##### **应用程序启动开启日志自动装配**

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

## 2.2 Spring NameSpace接入

##### **pom**

```xml
<dependency>
    <groupId>io.github.redick01</groupId>
    <artifactId>log-helper-spring</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

##### **Spring AOP配置**

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


## 2.3 Logback配置

由于该日志工具集成了`logback-logstash-encoder`，用于将日志格式化成`json`，所以在`logback`配置文件中指定日志格式配置是先决条件，配置如下：

+ logback.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false" scan="true" scanPeriod="30 minutes">
    <statusListener class="ch.qos.logback.core.status.NopStatusListener" />
    <!-- 引入外部配置文件的地址 -->
    <property resource="logback.properties"/>

    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>${STDOUT_LEVEL}</level>
        </filter>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <version>${LOG_VERSION}</version>
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
            <includeMdcKeyName>interface_name</includeMdcKeyName>
        </encoder>
    </appender>
    <!-- INFO级别的日志 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/${FILE_NAME}.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>${FILE_LEVEL}</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <!-- 文件扩展名设置为.zip/.gz后在文件滚动时会自动对旧日志进行压缩 -->
            <FileNamePattern>${LOG_HOME}/${FILE_NAME}.log.%d{yyyyMMdd}.%i.zip</FileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过512MB，若超过512MBM，日志文件会以索引0开始，命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>${FILE_MAX_SIZE}</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <MaxHistory>${FILE_HISTORY}</MaxHistory>
            <totalSizeCap>${FILE_TOTAL_SIZE}</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <version>${LOG_VERSION}</version>
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
            <includeMdcKeyName>interface_name</includeMdcKeyName>
        </encoder>
        <prudent>false</prudent>
    </appender>

    <!-- DEBUG级别的日志 -->
    <appender name="FILE_DEBUG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/debug/${FILE_NAME}_debug.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>${FILE_DEBUG_LEVEL}</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <!-- 文件扩展名设置为.zip/.gz后在文件滚动时会自动对旧日志进行压缩 -->
            <FileNamePattern>${LOG_HOME}/debug/${FILE_NAME}_debug.log.%d{yyyyMMdd}.%i.zip</FileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过512MB，若超过512MBM，日志文件会以索引0开始，命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>${FILE_DEBUG_MAX_SIZE}</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <MaxHistory>${FILE_DEBUG_HISTORY}</MaxHistory>
            <totalSizeCap>${FILE_DEBUG_TOTAL_SIZE}</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <version>${LOG_VERSION}</version>
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
            <includeMdcKeyName>interface_name</includeMdcKeyName>
        </encoder>
        <prudent>false</prudent>
    </appender>

    <!-- ERROR级别的日志 -->
    <appender name="FILE_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/error/${FILE_NAME}_error.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>${FILE_ERROR_LEVEL}</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <!-- 文件扩展名设置为.zip/.gz后在文件滚动时会自动对旧日志进行压缩 -->
            <FileNamePattern>${LOG_HOME}/error/${FILE_NAME}_error.log.%d{yyyyMMdd}.%i.zip</FileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过512MB，若超过512MBM，日志文件会以索引0开始，命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>${FILE_ERROR_MAX_SIZE}</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <MaxHistory>${FILE_ERROR_HISTORY}</MaxHistory>
            <totalSizeCap>${FILE_ERROR_TOTAL_SIZE}</totalSizeCap>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <version>${LOG_VERSION}</version>
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>request_type</includeMdcKeyName>
            <includeMdcKeyName>interface_name</includeMdcKeyName>
        </encoder>
        <prudent>false</prudent>
    </appender>

    <!-- 日志输出级别 -->

    <root>
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="FILE_DEBUG"/>
        <appender-ref ref="FILE_ERROR"/>
    </root>

</configuration>

```

+ logback.properties配置

```properties
#日志文件存储根路径
LOG_VERSION=0.0.1
LOG_HOME=logs
#日志文件名称前缀
FILE_NAME=spring-namespace-example
# 控制台日志打印级别
STDOUT_LEVEL=DEBUG
#INFO级别日志文件配置
#单个文件最大的大小
FILE_MAX_SIZE=512MB
#日志保留天数
FILE_HISTORY=10
#日志总大小
FILE_TOTAL_SIZE=40GB
# info级别日志打印开关，配置为INFO即打印，配置OFF即关闭
FILE_LEVEL=INFO
#DEBUG级别日志文件配置
FILE_DEBUG_MAX_SIZE=512MB
FILE_DEBUG_HISTORY=10
FILE_DEBUG_TOTAL_SIZE=40GB
# debug级别日志打印开关，配置为debug即打印debug级别的日志，配置OFF即关闭
FILE_DEBUG_LEVEL=DEBUG
#ERROR级别日志文件配置
FILE_ERROR_MAX_SIZE=512MB
FILE_ERROR_HISTORY=10
FILE_ERROR_TOTAL_SIZE=10GB
# error级别日志打印开关，配置为error即打印error级别的日志，配置OFF即关闭
FILE_ERROR_LEVEL=OFF
```


## 2.4 业务代码中使用

使用`@LogMarker`注解标注切面，示例如下：

```java
@RestController
public class TestController {

   private final String url = "http://localhost:8783/producer/say";

    @PostMapping("/httpclient")
    @LogMarker(businessDescription = "/httpclient-test", interfaceName = "com.redick.example.support.controller.ConsumerController#httpclient()")
    public @ResponseBody
    Response httpclient(@RequestBody Request requestDTO) {
        return JSONObject.parseObject(HttpClientUtil.doPost(url, JSONObject.toJSONString(requestDTO)), Response.class);
    }
}
```

日志内容：

```json
{"@timestamp":"2022-03-31T22:36:17.430+08:00","@version":"0.0.1","message":"开始处理","logger_name":"com.redick.example.support.controller.ConsumerController","thread_name":"http-nio-1743-exec-1","level":"INFO","level_value":20000,"traceId":"5dd5b5bc-c3f1-4090-9131-3e148edc5c6f","interface_name":"com.redick.example.support.controller.ConsumerController#httpclient()","request_type":"/httpclient-test","log_pos":"开始处理","data":[{"content":"test"}]}

{"@timestamp":"2022-03-31T22:36:18.746+08:00","@version":"0.0.1","message":"处理完毕","logger_name":"com.redick.example.support.controller.ConsumerController","thread_name":"http-nio-1743-exec-1","level":"INFO","level_value":20000,"traceId":"5dd5b5bc-c3f1-4090-9131-3e148edc5c6f","interface_name":"com.redick.example.support.controller.ConsumerController#httpclient()","request_type":"/httpclient-test","log_pos":"处理完毕","data":{"message":"success","data":"test","code":0},"duration":1298,"result":"成功"}
```





### 2.5 快速接入总结

基础的接入方式就这么多，下面了解下一些特殊场景支持的接入方式

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

## 3 其他支持

### 3.1 异步线程池链路追踪支持

#### 3.1.1 SpringBoot通过自动装配已经支持，无需多余配置

#### 3.1.2 Spring Namespace接入

&nbsp; &nbsp; Spring Namespace方式需要加载我们自己实现的`MDCAdapter`，程序启动加载进去就可以，这里我使用Spring事件监听，也可以使用`@PostContruct`等方式。

```java
@Component
public class ApplicationStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            TtlMDCAdapter.getInstance();
        }
    }
}
```

#### 3.1.3 示例

```java
@RestController
@Slf4j
public class TestController {

    @Resource(name = "ttlThreadPoolTaskExecutor")
    private TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor;

    @Resource(name = "ttlThreadPoolExecutor")
    private TtlThreadPoolExecutor ttlThreadPoolExecutor;

    @LogMarker(businessDescription = "say方法", interfaceName = "com.redick.loghelper.controller.TestController#say()")
    @GetMapping("/test")
    public String say(String content) {

        ttlThreadPoolExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolExecutor"), content);
        });

        ttlThreadPoolTaskExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolTaskExecutor"), content);
        });

        return "say" + content;
    }
}
```

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;


### 3.2 Apache Dubbo支持

#### 3.2.1 SpringBoot接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-apachedubbo</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
```

#### 3.2.2 Spring NameSpace接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-apachedubbo</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
```


### 3.3 Alibaba Dubbo支持

#### 3.3.1 SpringBoot接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-alibabadubbo</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
```

#### 3.3.2 Spring NameSpace接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-alibabadubbo</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
```

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

### 3.4 SpringCloud支持

#### 3.4.1 SpringBoot接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-openfeign</artifactId>
            <version>1.0-RELEASE</version>
        </dependency>
```

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

### 3.5 MQ消息队列

`log-helper-core`提供了对分布式消息队列MQ的`traceId`传递解决方案。

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

### 3.6 HttpClient，OkHttp，RestTemplate支持

`log-helper-core`提供了多种对`HttpClient`工具`traceId`传递的解决方案。

HttpClient，OkHttp，RestTemplate支持traceId需要代码入侵，具体实现方案是对HttpClient添加拦截器，拦截器的作用是消费者将traceId放到Http Header中，生产者从Http Header中获取traceId。

使用方式参数[示例](https://github.com/Redick01/log-helper/tree/master/log-helper-example/log-helper-example-support-httpclient)

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

### 3.7 接口参数操作

##### **接口请求参数脱敏：**

&nbsp; &nbsp; 由于打印的日志内容可能会是用户信息或者是密钥等敏感信息，所以打印日志时应该针对这部分敏感信息进行脱敏，此插件提供了一个`@Sensitive`注解，该注解作用于传入参数实体的具体属性上；该注解有两个参数`paramSensitiveType（参数脱敏类型）`表明该字段是针对身份证或银行卡或其它的类型进行脱敏，插件提供的SensitiveType类是脱敏类型定义类，isSensitive（是否需要脱敏），默认为false不脱敏，要脱敏时应设置为true，如果字段不需要脱敏不使用该注解即可。使用方法如下：

- `@Sensitive`注解使用方法：

```java
@Sensitive(start = 3, end = 10)
private String mac;
```

- 集合数据脱敏：

&nbsp; &nbsp; 有的服务接口传入参数不是以实体类的形式接收，而是以Map或List等数据结构，这样注解参数字段的方式已经不能实现参数脱敏，例如map，对此就需要规定需要规定死需要脱敏的参数key的命名，如下：
idCard,realName,bankCard,mobile,mac,macKey，分别是身份证号、姓名、银行卡号、电话号码、mac

##### **接口返回参数脱敏：**

&nbsp; &nbsp; 接口返回参数脱敏与接口传入参数脱敏方式相似，当返回值参数为java bean的时候正常使用`@Sensitive`注解；当java bean中存在范型的时候，并且范型类型也是java bean，范型类中字段内容中有需要脱敏的内容，要在范型类的字段上添加@Sensitive注解并且在具体的范型类型的java bean中的字段上也要正常使用@Sensitive字段。例如：

`resData`需要加脱敏注解，在实际业务中T实际的类要脱敏的字段也需要添加脱敏注解
```java
@Data
public class ModelsReturnT<T> {
    /**
    * 返回数据
    */
    @Sensitive
    private T resData;
    /**
    * 错误代码
    */
    private String resCode;
    /**
    * 错误信息
    */
    private String resMessage;
}
```


##### **方法内部参数脱敏：**

接口实现方法的参数脱敏可以使用AOP统一进行处理，但是在方法内部AOP无法做到灵活的处理，为了方便业务开发过程中能够更灵活地进行日志脱敏，在打印模版中提供了两个用于打印日志的模版方法：

- **commonSensitiveMarker：** 打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中不存在泛型参数；

- **genericSensitiveMarker：** 打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中存在需要脱敏的泛型参数，例如：ModelsReturn中的resData参数。

针对调用其他服务的接口如：dubbo和http接口，能更方便使用日志脱敏模版打印，在设计API和使用日志模版的时候提以下两点建议。

1. 设计支持脱敏的API：在设计API之初就考虑日志字段脱敏问题，例如服务提供方设计的API的java bean要做参数脱敏，即在java bean的字段上添加@Sensitive注解，这样当服务消费方在调用服务提供方提供的服务时服务消费方就能够很好的进行传入参数和响应参数的脱敏，此种方式特别是和dubbo接口；

2. 调用HTTP接口：在调用HTTP接口时因为请求参数是调用方自己定义的java bean，所以接口请求参数的脱敏非常灵活，只需要按要求使用@Sensitive注解即可，但是接口的响应参数会根据HTTP客户端的不同参数的包裹层级可能很多，建议序列化后进行参数脱敏。

##### **接口参数字段忽略不打印：**

在接口参数java bean的字段上添加@FieldIgnore注解即可实现。

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;


#### 3.8 MySQL执行时间统计支持

支持MySQL驱动拦截器，能够统计执行时间，使用方法如下：

- 3.8.1 MySQL 5

```xml
    <dependency>
        <groupId>io.github.redick01</groupId>
        <artifactId>mysql-5</artifactId>
        <version>1.0.3-RELEASE</version>
    </dependency>
```

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3316/log-helper?useUnicode=true&characterEncoding=UTF8&statementInterceptors=com.redick.support.mysql.Mysql5StatementInterceptor&serverTimezone=CST
```

- 3.8.2 MySQL 6

```xml
    <dependency>
        <groupId>io.github.redick01</groupId>
        <artifactId>mysql-6</artifactId>
        <version>1.0.3-RELEASE</version>
    </dependency>
```

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3316/log-helper?useUnicode=true&characterEncoding=UTF8&statementInterceptors=com.redick.support.mysql.Mysql6StatementInterceptor&serverTimezone=CST
```

- 3.8.3 MySQL 8

```xml
    <dependency>
        <groupId>io.github.redick01</groupId>
        <artifactId>mysql-8</artifactId>
        <version>1.0.3-RELEASE</version>
    </dependency>
```

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3316/log-helper?useUnicode=true&characterEncoding=UTF8&queryInterceptors=com.redick.support.mysql.Mysql8QueryInterceptor&exceptionInterceptors=Mysql8ExceptionInterceptor&serverTimezone=CST
```

### 3.9 Motan RPC框架支持

#### 3.9.1 SpringBoot接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-motan</artifactId>
            <version>1.0.3-RELEASE</version>
        </dependency>
```

#### 3.9.2 Spring NameSpace接入

```xml
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring</artifactId>
            <version>1.0.3-RELEASE</version>
        </dependency>
        <dependency>
            <groupId>io.github.redick01</groupId>
            <artifactId>log-helper-spring-boot-starter-motan</artifactId>
            <version>1.0.3-RELEASE</version>
        </dependency>
```

参考log-helper-example-motan示例程序

&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

## 4 日志打印自定义操作及建议规范

&nbsp; &nbsp; logback.xml的详细配置以及日志模板工具类（LogUtil）的使用参考：[日志打印自定义操作及建议规范](https://github.com/Redick01/log-helper/blob/master/use-detail.md)



&nbsp; &nbsp;
&nbsp; &nbsp;
&nbsp; &nbsp;

## 5 详细使用示例

参考[详细使用示例](https://github.com/Redick01/log-helper/tree/master/log-helper-example)


## 6 结合ELK系统效果

##### **服务消费者端日志效果**

![在这里插入图片描述](https://img-blog.csdnimg.cn/a31c54071c694ebfaa37c9247df56897.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5YiYcOi-iQ==,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

##### **服务提供者端日志效果**

![在这里插入图片描述](https://img-blog.csdnimg.cn/b829440146c44630bf3f94564f9f3d62.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5YiYcOi-iQ==,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)





![](https://img-blog.csdnimg.cn/ee26b66b058640ce986fcdfc58b7dbd8.png#pic_center)