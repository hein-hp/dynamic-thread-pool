<#macro appendPercentIfType type>
    <#if type == "LIVENESS" || type == "CAPACITY">
        %
    </#if>
</#macro>

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>【报警】${title}</title>
    <style>
        .title::before {
            content: "【报警】";
            color: rgba(255, 0, 0, 0.85);
        }

        ul {
            font-size: 16px;
            margin-left: 20px;
        }

        ul li {
            margin-top: 2px;
            margin-bottom: 2px;
        }
    </style>
</head>
<body style="background-color: #f5f5f5; font-family: sans-serif;">
<div style="max-width: 840px; margin: auto; background-color: #fff">
    <img src="cid:imageId" alt="" width="840px"/>
    <h3 class="title">${title}</h3>
    <ul>
        <li>服务名称：${serviceName}</li>
        <li>实例信息：${instanceInfo}</li>
        <li>环境：${environment}</li>
        <li>线程池名称：${threadPoolName}</li>
        <li>报警项：${alarmItemTypes}</li>
        <ul>
            <#list alarmItemList as item>
                <li>报警阈值 / 当前值：${item.threshold}<@appendPercentIfType item.type/> /
                    ${item.value}<@appendPercentIfType item.type/>（${item.type}）
                </li>
            </#list>
        </ul>
        <li>接受人：${receiver}</li>
        <li>报警时间：${alarmTime?datetime}</li>
    </ul>
    <h3>线程池相关配置</h3>
    <ul>
        <li>核心线程数：${corePoolSize}</li>
        <li>最大线程数：${maximumPoolSize}</li>
        <li>当前线程数：${poolSize}</li>
        <li>活跃线程数：${activeCount}</li>
    </ul>
    <h3>队列相关配置</h3>
    <ul>
        <li>队列类型：${queueType}</li>
        <li>队列容量：${queueCapacity}</li>
        <li>队列任务数量：${queueSize}</li>
        <li>队列剩余容量：${remainingCapacity}</li>
    </ul>
    <h3>拒绝策略相关配置</h3>
    <ul>
        <li>拒绝策略：${rejectedExecutionHandler}</li>
        <li>总拒绝次数：${rejectedTotalCount}</li>
    </ul>
</div>
</body>
</html>