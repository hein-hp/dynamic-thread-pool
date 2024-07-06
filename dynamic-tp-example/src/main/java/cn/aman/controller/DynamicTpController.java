package cn.aman.controller;

import cn.hein.core.executor.DynamicTpExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hein
 */
@Slf4j
@RestController
public class DynamicTpController {

    @Resource(name = "polymerizationShortlinkStats")
    private DynamicTpExecutor dynamicTpExecutor;

    @RequestMapping("/start")
    public String start() {
        for (int i = 0; i < 40; i++) {
            dynamicTpExecutor.execute(new Task());
        }
        return "started";
    }

    @RequestMapping("/stop")
    public String stop() {
        dynamicTpExecutor.shutdownNow();
        return "stop";
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(999999999);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
