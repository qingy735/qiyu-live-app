package org.qiyu.live.api.controller;

import jakarta.annotation.Resource;
import org.qiyu.live.api.service.ILivingRoomService;
import org.qiyu.live.api.vo.req.LivingRoomReqVO;
import org.qiyu.live.common.interfaces.vo.WebResponseVO;
import org.qiyu.live.framework.web.starter.context.QiyuRequestContext;
import org.qiyu.live.framework.web.starter.error.BizBaseErrorEnum;
import org.qiyu.live.framework.web.starter.error.ErrorAssert;
import org.qiyu.live.framework.web.starter.limit.RequestLimit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: QingY
 * @Date: Created in 20:48 2024-05-08
 * @Description:
 */
@RestController
@RequestMapping("/living")
public class LivingRoomController {

    @Resource
    private ILivingRoomService livingRoomService;

    @PostMapping("/list")
    public WebResponseVO list(LivingRoomReqVO livingRoomReqVO) {
        ErrorAssert.isTrue(livingRoomReqVO != null && livingRoomReqVO.getType() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTrue(livingRoomReqVO.getPage() > 0 && livingRoomReqVO.getPageSize() <= 100, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(livingRoomService.list(livingRoomReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "开播请求过于频繁，请稍后再试")
    @PostMapping("/startingLiving")
    public WebResponseVO startingLiving(Integer type) {
        // 调用rpc，往我们的开播表t_living_room写入一条记录
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        boolean startStatus = livingRoomService.startingLiving(type);
        if (startStatus) {
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("开播异常");
    }

    @RequestLimit(limit = 1, second = 10, msg = "关播请求过于频繁，请稍后再试")
    @PostMapping("/closeLiving")
    public WebResponseVO closeLiving(Integer roomId) {
        ErrorAssert.isNotNull(roomId, BizBaseErrorEnum.PARAM_ERROR);
        boolean closeStatus = livingRoomService.closeLiving(roomId);
        if (closeStatus) {
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("关播异常");
    }

    /**
     * 获取主播相关配置信息（只有主播才会有权限）
     *
     * @return
     */
    @PostMapping("/anchorConfig")
    public WebResponseVO anchorConfig(Integer roomId) {
        long userId = QiyuRequestContext.getUserId();
        return WebResponseVO.success(livingRoomService.anchorConfig(userId, roomId));
    }

}
