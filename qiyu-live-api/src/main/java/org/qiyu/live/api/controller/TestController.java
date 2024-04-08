package org.qiyu.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.user.interfaces.IUserRpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: QingY
 * @Date: Created in 14:49 2024-04-07
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/dubbo")
    public String dubbo() {
        return userRpc.test();
    }
}
