# 详细使用文档


## logback.xml详细配置

- **标准字段**

| 领域 | 描述 |
| --- | --- |
| @timestamp | 日志事件的时间。（yyyy-MM-dd'T'HH:mm:ss.SSSZZ） |
| @version | Logstash格式版本（例如1） |
| message | 事件的格式化日志消息 |
| logger_name | 记录事件的记录器的名称 |
| thread_name | 记录事件的线程的名称 |
| level | 事件级别的字符串名称 |
| level_value | 事件级别的整数值 |
| stack_trace | （仅当记录了throwable时）throwable的stacktrace。Stackframes由行结尾分隔。|
| tags | 仅当找到标签时）未明确处理的任何标记的名称。（例如，标记MarkerFactory.getMarker将作为标记包含在内，但标记来自Markers不会。）可以通过<includeTags>false</includeTags>在编码器/布局/附加配置中指定来完全禁用。 |

- **MDC字段**

默认情况下，映射的诊断上下文（MDC）（org.slf4j.MDC）中的每个条目都将显示为LoggingEvent中的字段。也可以通过以下方式配置包含或排除全局MDC字段

> 包含：

```xml
<encoder class = "net.logstash.logback.encoder.LogstashEncoder">
  <includeMdcKeyName>key1ToInclude</includeMdcKeyName>
  <includeMdcKeyName>key2ToInclude</includeMdcKeyName>
</encoder>
```

> 排除

```xml
<encoder class = "net.logstash.logback.encoder.LogstashEncoder">
  <excludeMdcKeyName>key1ToExclude</excludeMdcKeyName>
  <excludeMdcKeyName>key2ToExclude</excludeMdcKeyName>
</encoder>
```
> 自定义标准字段
自定义字段可以将标准字段的改成自定义的，但是在标准字段已经与运维确定后就不要做任何修改了，防止ELK解析不了打印的日志，以下是修改标准字段原因
```xml
<encoder class = "net.logstash.logback.encoder.LogstashEncoder">
  <fieldNames>
    <timestamp>time</timestamp>
    <message>msg</message>
    ...
  </fieldNames>
</encoder>
```
> 自定义版本
```xml
<encoder  class ="net.logstash.logback.encoder.LogstashEncoder">
  <version>2</version>
</encoder>
```
> 该值可以写为数字（而不是字符串）
```xml
<encoder  class = "net.logstash.logback.encoder.LogstashEncoder">
  <writeVersionAsInteger> true </writeVersionAsInteger>
</encoder>
```
> 自定义时间戳
默认情况下，时间戳以格式yyyy-MM-dd'T'HH:mm:ss.SSSZZ（例如2018-04-28T22:23:59.164-07:00）在主机Java平台的默认TimeZone 中写为字符串值。
```xml
<timestampPattern>yyyy-MM-dd HH：mm：ss.SSS</timestampPattern>
```

## 日志模版介绍

该插件提供了一个打印日志模版类，aop只提供了打印接口中的通用信息，接口内部详细日志还需要程序员自行打印，打印日志（结合logback）就需要用到工具中提供的日志模版，打印日志模版内容及打印方式如下：

```java
public class LogUtil {
    public static final String kLOG_KEY_TYPE = "type";
    public static final String kLOG_KEY_DATA = "data";
    public static final String kLOG_KEY_DURATION = "duration";
    public static final String kLOG_KEY_GLOBAL_SESSION_ID_KEY = "x_global_session_id";
    public static final String kLOG_KEY_REQUEST_TYPE = "request_type";
    public static final String kLOG_KEY_RESULT = "result";
    public static final String kTYPE_BEGIN = "开始处理";
    public static final String kTYPE_DONE = "处理完毕";
    public static final String kTYPE_FUNC_START = "调用第三方开始";
    public static final String kTYPE_FUNC_END = "调用第三方结束";
    public static final String kTYPE_BIZ = "业务状态变更";
    public static final String kTYPE_BRANCH = "分支";
    public static final String kTYPE_PROCESSING = "过程";
    public static final String kTYPE_EXCEPTION = "异常";
    public static final String kRESULT_SUCCESS = "成功";
    public static final String kRESULT_FAILED = "失败";

    private static final LogstashMarker defaultMarker = append(kLOG_KEY_TYPE, kTYPE_PROCESSING);
    private static final LogstashMarker exceptionMarker = append(kLOG_KEY_TYPE, kTYPE_EXCEPTION);

    /**
     * 打印日志模版
     * @param type 类型
     * @param data 数据
     * @return
     */
    public static LogstashMarker marker(String type, Object data) {
        LogstashMarker result = append(kLOG_KEY_TYPE, type);
        if (data != null) {
            result.and(append(kLOG_KEY_DATA, data));
        }
        return result;
    }

    /**
     * 接口处理成功日志打印模版
     * @param data
     * @param duration
     * @return
     */
    public static LogstashMarker processSuccessDoneMarker(Object data, String duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));

        result.and(append(kLOG_KEY_RESULT, kRESULT_SUCCESS));

        return result;
    }

    /**
     * 接口处理失败日志打印模版
     * @param data
     * @param duration
     * @return
     */
    public static LogstashMarker processFailedDoneMarker(Object data, String duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));

        result.and(append(kLOG_KEY_RESULT, kRESULT_FAILED));

        return result;
    }

    /**
     * 处理完毕日志打印模版
     * @param duration
     * @return
     */
    public static LogstashMarker processDoneMarker(String duration) {
        LogstashMarker result = marker(kTYPE_DONE).and(append(kLOG_KEY_DURATION, duration));
        return result;
    }

    /**
     * 接口过程开始日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker processBeginMarker(Object data) {
        return marker(kLOG_KEY_REQUEST_TYPE, data);
    }

    /**
     * 接口执行过程日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker marker(Object data) {
        return marker(kTYPE_PROCESSING, data);
    }

    /**
     * 开始调用第三方接口日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker funcStartMarker(Object data) {
        return marker(kTYPE_FUNC_START, data);
    }

    /**
     * 结束调用第三方接口日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker funcEndMarker(Object data) {
        return marker(kTYPE_FUNC_END, data);
    }

    /**
     * 业务状态变更日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker bizMarker(Object data) {
        return marker(kTYPE_BIZ, data);
    }

    /**
     * 分之日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker branchMarker(Object data) {
        return marker(kTYPE_BRANCH, data);
    }

    /**
     * 打印日志默认模版
     * @return
     */
    public static LogstashMarker marker() {
        return defaultMarker;
    }

    /**
     * 异常日志打印模版
     * @return
     */
    public static LogstashMarker exceptionMarker() {
        return exceptionMarker;
    }
    /**
     * 需要自定义request_type打印日志时调用
     * @param processDescription 过程描述（文字描述）
     * @param data 打印数据（可以为null）
     * @param type 日志类型 （如：kTYPE_BEGIN）
     * @return
     */
    public static LogstashMarker requestTypeMarker(String processDescription, Object data, String type) {
        LogstashMarker result = marker(type, data);
        String businessDescription = MDC.get(kLOG_KEY_REQUEST_TYPE);
        result.and(append(kLOG_KEY_REQUEST_TYPE, businessDescription + "." + processDescription));
        return result;
    }

    /**
     * 打印脱敏数据日志模版
     * @param data
     * @param type
     * @return
     */
    public static LogstashMarker sensitiveInfoConvertMarker(Object data, String type) {
        String str = SensitiveDataConverter.invokeMsg(data.toString());
        LogstashMarker result = marker(type, str);
        return result;
    }
}
```

- **日志模版使用方法**

1. 在接口方法上添加@LogMarker注解，aop会处理标有该注解的接口方法，打印接口调用时间、请求参数、返回参数、全局会话ID、request_type等信息；该注解有两个参数interfaceName（接口名称,可以不写），businessDescription（处理业务类型，必须写，且是汉字说明）这两个注解会在日志的request_type中体现。注解例如：
@LogMarker(businessDescription = "根据业务获取支付渠道列表")

2. 使用模版打印接口内部日志：在程序条件转折，调用第三方接口都要打印相应模版的日志。
3. request_type说明：request_type字段中包含最少两个内容，例如："request_type":"根据业务获取支付渠道列表.获取已签约支付渠道", 这里的“根据业务获取支付渠道列表”是整个接口的业务描述，通过注解businessDescription来配置，“获取已签约支付渠道”这部分的内容是只大业务中包含的小业务，如：“调用户中心验mac”、“下发黑名单”、“连接支付平台”等等，这部分需要自己去在打日志过程中手动写，通过此模版可设置大业务接口的子业务描述public static LogstashMarker requestTypeMarker(String processDescription, Object data, String type)，注：只有调用第三方或小业务变更需要填写子业务描述，其他日志可自选模版打印。

- **日志记录规则**

1.	日志应该准确清晰，禁止使用模糊的字眼表述或封装原本清晰的异常提示。
2.	禁止输出装饰字符如 “------start process------”。
3.	可以合并到一条日志中输出的内容，禁止分散成多条。
4.	日志的内容部分只应该输出对人可读的、友好的信息；参数、变量、状态、应答等数据应该输出到数据域。
5.	建议将引入的第三方类库日志级别设置成WARN，有必要保留第三方类库日志时可例外处理。
6.	try/catch语句块中禁止使用类似 System.out / e.printStackTrace导致异常发生悄无声息；禁止catch异常后不做任何处理静默吃掉。
7.	在可以预知的异常（比如底层抛出业务定义的参数异常等）或明确异常发生原因时，可以只记录异常必要的Message信息；在不可预知时必须打印完整的stack trace。
8.	规范日志级别的使用，对于不满足某些业务条件但是正常的请求不应使用比INFO更高的级别来展示，除非这种情况应该引起相关人员注意，需要得到控制或处理，比如调用第三方服务超时。
9.	对于运行过程中重要程度不高的状态变化等信息，建议使用DEBUG级别来记录，减少INFO级别日志分析难度与数量，使之更适合统计分析。
10.	禁止日志中包含用户或系统的敏感信息（如用户身份证号、密码、API key、API secret等），必要时可以脱敏后展示，可以用“*”掩盖部分敏感信息或使用其他方式对敏感信息做摘要、加密处理后输出。
11.	调用外部系统或模块的开始、过程、结果，应根据需要记录日志，必须记录调用耗时情况，便于后期排错查找具体错误原因。
12.	处理DEBUG日志时必须考虑性能，当日志内容简单时可以考虑直接记录；当日志内容涉及复杂的拼接或处理过程时，应在记录前先通过 isDebugEnabled 方法检查是否开启了DEBUG级别日志，再做记录，避免不必要的性能消耗。
13.	禁止在日志语句中使用 “+” 做字符串拼接，尽可能使用变量替换的方式处理。
14.	多线程处理的情况下，要求日志记录请求信息或线程信息，否则日志混在一起毫无用处。可以使用MDC做相关处理。建议使用与请求有关的，可以连贯上下文的数据作为标识；或者在上下文不重要的场景中，给请求生成一个唯一ID便于快速定位日志。
15.	代码中需要处理日志时，必须使用slf4j，不应使用任何一个Log方案的具体实现。
16.	如无其他特殊需求，要求使用logback作为后端日志记录的具体实现，示例配置文件参见本文附件 “logback-sample-v2.0.0.xml”
17.	要求日志输出必须为合法的JSON对象（按照示例配置，并引用logstash-logback-encoder组件）。
18.	要求日志至少提供如下数据域（示例配置文件已经完成了相关配置）：
a)	@timestamp（时间戳）
b)	@version（版本）
c)	message（日志消息文本）
d)	logger_name
e)	thread_name
f)	level
g)	level_value（级别数值）
h)	request_type（请求类型，详见下文）
i)	x_global_session_id（会话ID，详见下文）
j)	type（日志类型，详见下文）
k)	data（与该日志相关的数据，可以为空，详见下文）
l)	duration（处理该请求消耗的时间）
m)	result（请求处理结果，“成功”或“失败”）
19.	日志必须用中文清晰描述请求类型，在不违背其他规范的前提下尽量打印调用参数和返回结果。
20.	对于dubbo服务提供者，必须引用global-session-id-filter（com.ruubypay.common:global-session-id-filter:1.0.2），并在日志配置中中要求打印MDC的“x_global_session_id”变量，为日志提供全局会话id便于排查问题。
21.	对于dubbo的消费者，必须引用global-session-id-filter（com.ruubypay.common:global-session-id-filter:1.0.2），并在进行业务处理之前自行生成session id（建议使用随机uuid），将session id设置到MDC的“x_global_session_id”变量，后续调用其他dubbo服务时插件会自动传递全局会话id。
22.	日志中应该包含类型字段(type)，日志信息类型包括：“开始处理”、“处理完毕”、“过程”、“异常”、“调用第三方开始”、“调用第三方结束”、“业务状态变更”，常量均已定义在LogHelper类中。每个业务请求必须输出仅一条“开始处理”及“处理完毕”日志；在必要的调用第三方类库或服务的场景时输出“调用第三方开始”、“调用第三方结束”日志；“过程”、“业务状态变更”及“异常”日志按需输出。
23.	“业务开始处理”、“业务处理完毕”这两条日志应当使用INFO级别，业务处理完毕日志应包含业务处理消耗的时间，输出到duration字段，时间为数值类型，单位统一为毫秒；在业务处理消耗的时间大于预期时，应该将业务处理完毕日志等级提高到WARN甚至更高。
24.	业务处理中发生的异常必须使用INFO或更高级别。
25.	输出与日志相关的数据时，应提前组织数据对象，并使用Marker的方式将其输出到data数据域，具体做法可参考本文附件示例。
26.	使用AOP等方式打印日志时，禁止将辅助工具类的名称代替实现类来打印日志，具体做法可参考本文附件示例，但本规范并不限定必须使用AOP来完成日志打印，也可以自主控制日志输出。
27.	重要的条件分支前后应该打印日志，确定到底走了哪个分支。

## 打印日志示例代码

1. 异常日志打印：
```java 
logback.error(LogUtil.exceptionMarker(),"未知异常", e);
```
2. request_type自定义小业务描述：
```java 
logback.debug(LogUtil.requestTypeMarker("获取已签约支付渠道", signPayChannelList, LogUtil.kTYPE_PROCESSING), "");
```
3. 调用第三方接口日志打印：
```java 
logback.debug(LogUtil.funcStartMarker(signPayChannelList), "调用第三方开始"); logback.debug(LogUtil.funcEndMarker(signPayChannelList), "调用第三方结束");
```
4. 业务状态变更：
```java 
logback.debug(LogUtil.bizMarker(signPayChannelList), "业务状态变更描述");
```
5. 分支日志打印：
```java 
logback.debug(LogUtil.branchMarker(signPayChannelList), "分支描述");
```

## 日志内容操作

```
（1）接口请求参数脱敏：
   由于接口传入参数可能会传入用户信息或者是密钥等敏感信息，所以打印日志时应该针对这部分敏感信息进行脱敏，此插件提供了一个@Sensitive注解，该注解作用于传入参数实体的具体属性上；该注解有两个参数paramSensitiveType（参数脱敏类型）表明该字段是针对身份证或银行卡或其它的类型进行脱敏，插件提供的SensitiveType类是脱敏类型定义类，isSensitive（是否需要脱敏），默认为false不脱敏，要脱敏时应设置为true，如果字段不需要脱敏不使用该注解即可。使用方法如下：
   a.@Sensitive注解使用方法：
@Sensitive(paramSensitiveType = SensitiveType.MAC, isSensitive = true)
private String mac;
   b.集合数据脱敏：
   有的服务接口传入参数不是以实体类的形式接收，而是以Map或List等数据结构，这样注解参数字段的方式已经不能实现参数脱敏，例如map，对此就需要规定需要规定死需要脱敏的参数key的命名，如下：
   idCard,realName,bankCard,mobile,mac,macKey，分别是身份证号、姓名、银行卡号、电话号码、mac、mac
（2）接口返回参数脱敏：
   接口返回参数脱敏与接口传入参数脱敏方式相似，当返回值参数为java bean的时候正常使用@Sensitive注解；当java bean中存在范型的时候，并且范型类型也是java bean，范型类中字段内容中有需要脱敏的内容，要在范型类的字段上添加@Sensitive注解并且在具体的范型类型的java bean中的字段上也要正常使用@Sensitive字段。例如：
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
resData需要加脱敏注解，在实际业务中T实际的类要脱敏的字段也需要添加脱敏注解
（3）方法内部参数脱敏：
   接口实现方法的参数脱敏可以使用AOP统一进行处理，但是在方法内部AOP无法做到灵活的处理，为了方便业务开发过程中能够更灵活地进行日志脱敏，在打印模版中提供了两个用于打印日志的模版方法：
   commonSensitiveMarker：打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中不存在泛型参数；
   genericSensitiveMarker：打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中存在需要脱敏的泛型参数，例如：ModelsReturn中的resData参数。
   针对调用其他服务的接口如：dubbo和http接口，能更方便使用日志脱敏模版打印，在设计API和使用日志模版的时候提以下两点建议。
   a.设计支持脱敏的API：在设计API之初就考虑日志字段脱敏问题，例如服务提供方设计的API的java bean要做参数脱敏，即在java bean的字段上添加@Sensitive注解，这样当服务消费方在调用服务提供方提供的服务时服务消费方就能够很好的进行传入参数和响应参数的脱敏，此种方式特别是和dubbo接口；
   b.调用HTTP接口：在调用HTTP接口时因为请求参数是调用方自己定义的java bean，所以接口请求参数的脱敏非常灵活，只需要按要求使用@Sensitive注解即可，但是接口的响应参数会根据HTTP客户端的不同参数的包裹层级可能很多，建议序列化后进行参数脱敏。
（4）接口参数忽略不打印：
    在接口参数java bean的字段上添加@FieldIgnore注解即可。
```