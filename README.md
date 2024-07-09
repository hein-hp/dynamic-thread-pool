# dynamic-thread-pool
## 背景

三大问题：

1. 线程池参数不好配置
2. 修改线程池参数需要重启服务
3. 无法有效监控线程池状态

## 解决

基于 Nacos 配置中心、深度集合 SpringBoot 实现：

1. 动态调整线程池参数
2. 线程池参数变更通知
3. 线程池指标告警通知

参考：[Java线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html) 

## 使用

### 引入依赖

```xml
<dependency>
    <groupId>cn.hein</groupId>
    <artifactId>dynamic-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

### 编写 bootstrap.yaml 文件

示例：

```yaml
spring:
  application:
    name: dynamic-tp-example
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: server-addr
        username: username
        password: password
      config:
        server-addr: server-addr
        file-extension: yaml
        group: DEFAULT_GROUP
        username: username
        password: password
        extension-configs:
          - data-id: dynamic-tp-example
            group: DEFAULT_GROUP
            refresh: true
  # 配置邮件
  mail:
    host: smtp.qq.com
    username: username
    password: password # password 为邮箱授权码
    port: 587
    default-encoding: UTF-8
    properties:
      mail:
        smtp:
          ssl:
            enable: false
            required: false
        debug: true
```

### 配置中心

在 Nacos 创建 dataId 为 dynamic-tp-example 的配置文件，内容如下：

```yaml 
dynamic:
  thread-pool:
    enabled: true # 是否开启动态线程池
    change: 
      enabled: true
      platform-key: hein
    monitor:
      enabled: true # 是否开启监控
      interval: 5 # 监控间隔时长
      time-unit: SECONDS
    platforms: # 告警平台
      - key: hein # unique key for identify
        platform: email
        receiver: hein.hp@foxmail.com # 邮箱
        enabled: true
      - key: hihi # unique key for identify
        platform: email
        receiver: hein.hp@foxmail.com # 邮箱
        enabled: true
    executors: # 动态线程池配置
      - thread-pool-name: polymerization-shortlink-stats # 转驼峰后作为 BeanName
        core-pool-size: 30 # 核心线程数
        maximum-pool-size: 40 # 最大线程数
        queue-type: ResizeLinkedBlockingQueue # 阻塞队列类型
        queue-capacity: 10 # 阻塞队列容量
        keep-alive-time: 60 # 线程空闲时间
        time-unit: SECONDS # 时间单位
        allow-core-thread-timeout: true # 是否允许核心线程池超时，默认 false
        rejected-execution-handler: ABORT # 拒绝策略
        executor-name-prefix: shortlink-stats # 线程名称前缀
        monitor-enable: false # 是否开启监控
        notify:
          enabled: false # 是否开启告警
          platform-key: hihi
          notify-interval: 60 # 通知间隔时长
          notify-time-unit: SECONDS
          notify-item:
            - type: capacity # 阻塞队列使用率告警
              enabled: true
              threshold: 80
            - type: liveness # 线程池活跃性告警
              enabled: true
              threshold: 80
            - type: reject # 拒绝策略告警
              enabled: true
              threshold: 20
            - type: timeout # 任务执行超时告警
              enabled: true
              threshold: 20
```

### 注入实例

在项目中直接注入 Bean 即可。

```java
@Resource(name = "polymerizationShortlinkStats")
private DynamicTpExecutor dynamicTpExecutor;
```

- 建议使用 @Resource 注明 name 属性
- 若使用 @Autowired 请另外配置 @Qualifier
- 不推荐使用构造注入，若存在多个动态线程池实例情况下会导致错误
