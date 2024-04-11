package org.qiyu.live.user.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: QingY
 * @Date: Created in 13:56 2024-04-11
 * @Description:
 */
public class UserCacheAsyncDeleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6175697630986105822L;

    /**
     * 不同code区分不同延迟消息
     */
    private int code;
    private String json;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "UserCacheAsyncDeleteDTO{" +
                "code=" + code +
                ", json='" + json + '\'' +
                '}';
    }
}
