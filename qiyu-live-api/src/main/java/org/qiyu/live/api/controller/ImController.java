package org.qiyu.live.api.controller;

import jakarta.annotation.Resource;
import org.qiyu.live.api.service.ImService;
import org.qiyu.live.api.vo.resp.ImConfigVO;
import org.qiyu.live.common.interfaces.vo.WebResponseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: QingY
 * @Date: Created in 19:03 2024-05-09
 * @Description:
 */
@RestController
@RequestMapping("/im")
public class ImController {

    @Resource
    private ImService imService;

    // im地址
    // token
    @PostMapping("/getImConfig")
    public WebResponseVO getImConfig() {
        ImConfigVO imConfig = imService.getImConfig();
        return WebResponseVO.success(imConfig);
    }

}
