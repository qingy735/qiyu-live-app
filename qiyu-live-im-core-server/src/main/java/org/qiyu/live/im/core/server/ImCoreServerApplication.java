package org.qiyu.live.im.core.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Netty启动类
 *
 * @Author: QingY
 * @Date: Created in 20:57 2024-04-27
 * @Description:
 */
@SpringBootApplication
@EnableDubbo
public class ImCoreServerApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication springApplication = new SpringApplication(ImCoreServerApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
