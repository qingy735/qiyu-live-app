package org.qiyu.live.user.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.qiyu.live.user.constants.UserTagsEnum;
import org.qiyu.live.user.dto.UserDTO;
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

    @Resource
    private IUserTagService userTagService;
    @Resource
    private IUserService userService;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserProviderApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Long userId = 1012L;
        UserDTO userDTO = userService.getById(userId);
        LOGGER.info("查询到的用户信息为：{}", userDTO);
        // userDTO.setNickName("二次删除test");
        // userService.updateUserInfo(userDTO);

        userId = 10001L;
        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
        // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER));
        // System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
        // System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_OLD_USER));
        // System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
    }
}
