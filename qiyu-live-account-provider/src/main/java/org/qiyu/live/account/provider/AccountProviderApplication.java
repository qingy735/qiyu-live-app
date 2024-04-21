package org.qiyu.live.account.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.qiyu.live.account.provider.service.IAccountTokenService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: QingY
 * @Date: Created in 17:31 2024-04-20
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class AccountProviderApplication implements CommandLineRunner {

    @Resource
    private IAccountTokenService accountTokenService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AccountProviderApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Long userId = 10012L;
        // String token = accountTokenService.createAndSaveLoginToken(userId);
        // System.out.println("token is : " + token);
        // Long userIdByToken = accountTokenService.getUserIdByToken(token);
        // System.out.println("userIdByToken is: " + userIdByToken);
    }
}
