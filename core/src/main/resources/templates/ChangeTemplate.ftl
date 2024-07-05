<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>【通知】${title}</title>
    <style>
        .title::before {
            content: "【通知】";
            color: #008001;
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
        <#list changeList as change>
            <p style="font-weight: bold">线程池名称：${change.threadPoolName}</p>
            <ul>
                <li>核心线程数：${change.oldCorePoolSize} => ${change.newCorePoolSize}</li>
                <li>最大线程数：${change.oldMaximumPoolSize} => ${change.newMaximumPoolSize}</li>
                <li>允许核心线程超时：${change.oldAllowCoreThreadTimeOut?c} => ${change.newAllowCoreThreadTimeOut?c}</li>
                <li>线程存活时间：${change.oldKeepAliveTime} ${change.oldTimeUnit} => ${change.newKeepAliveTime} ${change.newTimeUnit} </li>
                <li>队列类型：${change.oldQueueType} => ${change.newQueueType}</li>
                <li>队列容量：${change.oldQueueCapacity} => ${change.newQueueCapacity}</li>
                <li>拒绝策略：${change.oldRejectedExecutionHandler} => ${change.newRejectedExecutionHandler}</li>
            </ul>
        </#list>
        <li>接收人：${receiver}</li>
        <li>变更时间：${changeTime?datetime}</li>
    </ul>
</div>
</body>
</html>