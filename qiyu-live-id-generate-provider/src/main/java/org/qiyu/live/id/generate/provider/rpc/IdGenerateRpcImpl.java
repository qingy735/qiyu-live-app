package org.qiyu.live.id.generate.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.id.generate.interfaces.IdGenerateRpc;

/**
 * @Author: QingY
 * @Date: Created in 14:59 2024-04-09
 * @Description:
 */
@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {
    @Override
    public Long getSeqId(Integer id) {
        return null;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return null;
    }
}
