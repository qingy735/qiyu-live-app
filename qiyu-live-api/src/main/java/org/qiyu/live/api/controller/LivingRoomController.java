package org.qiyu.live.api.controller;

import jakarta.annotation.Resource;
import org.qiyu.live.api.service.ILivingRoomService;
import org.qiyu.live.api.vo.req.LivingRoomReqVO;
import org.qiyu.live.common.interfaces.vo.WebResponseVO;
import org.qiyu.live.framework.web.starter.QiyuRequestContext;
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
        if (livingRoomReqVO == null || livingRoomReqVO.getType() == null) {
            return WebResponseVO.errorParam("需要给定直播间类型");
        }
        if (livingRoomReqVO.getPage() <= 0 || livingRoomReqVO.getPageSize() > 100) {
            return WebResponseVO.errorParam("分页查询参数错误");
        }
        return WebResponseVO.success(livingRoomService.list(livingRoomReqVO));
    }

    @PostMapping("/startingLiving")
    public WebResponseVO startingLiving(Integer type) {
        // 调用rpc，往我们的开播表t_living_room写入一条记录
        if (type == null && type > 0) {
            return WebResponseVO.errorParam("需要给定直播间类型");
        }
        boolean startStatus = livingRoomService.startingLiving(type);
        if (startStatus) {
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("开播异常");
    }

    @PostMapping("/closeLiving")
    public WebResponseVO closeLiving(Integer roomId) {
        if (roomId == null) {
            return WebResponseVO.errorParam("需要给定直播间id");
        }
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
        return WebResponseVO.success(livingRoomService.anchorConfig(userId,roomId));
    }

}
