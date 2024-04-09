package org.qiyu.live.id.generate.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.id.generate.provider.service.IdGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: QingY
 * @Date: Created in 15:01 2024-04-09
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IdGenerateProvider implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateProvider.class);
    @Resource
    private IdGenerateService idGenerateService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateProvider.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    public void run(String... args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            Long id = idGenerateService.getSeqId(1);
            System.out.println(id);
        }
    }
}
