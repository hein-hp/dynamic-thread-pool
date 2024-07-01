package cn.hein.common.toolkit;

import java.util.concurrent.*;

public class ThreadPoolInfoPrinter {

    public static void printThreadPoolInfo(ThreadPoolExecutor executor) {
        System.out.println("ThreadPool Info:");
        System.out.println("--------------------------------------------------------");
        System.out.println("ThreadPool Name: " + executor.toString());
        System.out.println("Core Pool Size: " + executor.getCorePoolSize());
        System.out.println("Maximum Pool Size: " + executor.getMaximumPoolSize());
        System.out.println("Current Pool Size: " + executor.getPoolSize());
        System.out.println("Largest Pool Size Ever: " + executor.getLargestPoolSize());
        System.out.println("Active Threads: " + executor.getActiveCount());
        System.out.println("Task Count (Total Queued & Running): " + executor.getTaskCount());
        System.out.println("Completed Task Count: " + executor.getCompletedTaskCount());
        System.out.println("Allow CoreThread Timeout: " + executor.allowsCoreThreadTimeOut());
        System.out.println("Keep Live Time: " + executor.getKeepAliveTime(TimeUnit.SECONDS) + "S");

        BlockingQueue<Runnable> queue = executor.getQueue();
        System.out.println("Queue Type: " + queue.getClass().getName());
        System.out.println("Queue Size: " + queue.size());
        System.out.println("Queue Remaining Capacity: " + queue.remainingCapacity());

        RejectedExecutionHandler handler = executor.getRejectedExecutionHandler();
        System.out.println("Rejected Execution Handler: " + handler.getClass().getName());


        System.out.println("--------------------------------------------------------");
    }
}