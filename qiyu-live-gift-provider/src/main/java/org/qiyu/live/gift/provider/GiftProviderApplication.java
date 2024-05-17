package org.qiyu.live.gift.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.qiyu.live.gift.dto.GiftConfigDTO;
import org.qiyu.live.gift.provider.service.IGiftConfigService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * @Author idea
 * @Date: Created in 14:49 2023/7/30
 * @Description
 */
@SpringBootApplication
@EnableDubbo
public class GiftProviderApplication implements CommandLineRunner {

    @Resource
    private IGiftConfigService giftConfigService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<GiftConfigDTO> giftConfigDTOList = giftConfigService.queryGiftList();
        for (GiftConfigDTO giftConfigDTO : giftConfigDTOList) {
            GiftConfigDTO queryFromRedis = giftConfigService.getByGiftId(giftConfigDTO.getGiftId());
            System.out.println(queryFromRedis);
            //修改名称
            queryFromRedis.setGiftName(queryFromRedis.getGiftName() + "-2");
//            giftConfigService.updateOne(queryFromRedis);
//            queryFromRedis.setGiftId(null);
            //插入一个新的记录
//            giftConfigService.insertOne(queryFromRedis);
        }
    }
}
