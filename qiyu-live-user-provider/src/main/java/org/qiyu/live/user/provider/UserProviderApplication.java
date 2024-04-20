package org.qiyu.live.user.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.qiyu.live.user.constants.UserTagsEnum;
import org.qiyu.live.user.dto.UserDTO;
import org.qiyu.live.user.dto.UserLoginDTO;
import org.qiyu.live.user.provider.service.IUserPhoneService;
import org.qiyu.live.user.provider.service.IUserService;
import org.qiyu.live.user.provider.service.IUserTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: QingY
 * @Date: Created in 21:36 2024-04-03
 * @Description: 用户中台服务提供者
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserProviderApplication.class);
    @Resource
    private IUserPhoneService userPhoneService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserProviderApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        String phone = "13878563676";
        UserLoginDTO userLoginDTO = userPhoneService.login(phone);
        System.out.println(userLoginDTO);
        System.out.println(userPhoneService.queryByUserId(userLoginDTO.getUserId()));
        System.out.println(userPhoneService.queryByUserId(userLoginDTO.getUserId()));
        System.out.println(userPhoneService.queryByPhone(phone));
        System.out.println(userPhoneService.queryByPhone(phone));
    }
}
