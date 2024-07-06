package cn.aman;

import cn.hein.annotation.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDynamicTp
@SpringBootApplication
public class DynamicTpExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicTpExampleApplication.class, args);
    }
}
