package org.qiyu.live.im.core.server.interfaces.dto;

import java.io.Serializable;

/**
 * @Author: QingY
 * @Date: Created in 20:00 2024-05-09
 * @Description:
 */
public class ImOfflineDTO implements Serializable {
    private Long userId;
    private Integer appId;
    private Integer roomId;

    private Long loginTime;

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
