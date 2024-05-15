package org.qiyu.live.api.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.qiyu.live.api.service.ImService;
import org.qiyu.live.api.vo.resp.ImConfigVO;
import org.qiyu.live.framework.web.starter.context.QiyuRequestContext;
import org.qiyu.live.im.constants.AppIdEnum;
import org.qiyu.live.im.interfaces.ImTokenRpc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 19:04 2024-05-09
 * @Description:
 */
@Service
public class ImServiceImpl implements ImService {

    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public ImConfigVO getImConfig() {
        ImConfigVO imConfigVO = new ImConfigVO();
        imConfigVO.setToken(imTokenRpc.createImLoginToken(QiyuRequestContext.getUserId(), AppIdEnum.QIYU_LIVE_BIZ.getCode()));
        buildImServerAddress(imConfigVO);
        return imConfigVO;
    }

    public void buildImServerAddress(ImConfigVO imConfigVO) {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("qiyu-live-im-core-server");
        Collections.shuffle(serviceInstanceList);
        ServiceInstance aimInstance = serviceInstanceList.get(0);
        imConfigVO.setWsImServerAddress(aimInstance.getHost() + "8086");
        imConfigVO.setTcpImServerAddress(aimInstance.getHost() + "8085");
    }


}
