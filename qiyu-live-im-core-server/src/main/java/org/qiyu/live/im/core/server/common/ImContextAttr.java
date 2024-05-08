package org.qiyu.live.im.core.server.common;

import io.netty.util.AttributeKey;

/**
 * @Author: QingY
 * @Date: Created in 21:53 2024-04-29
 * @Description:
 */
public class ImContextAttr {
    /**
     * 绑定用户id
     */
    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

    /**
     * 绑定appId
     */
    public static AttributeKey<Integer> APP_ID = AttributeKey.valueOf("appId");
}
