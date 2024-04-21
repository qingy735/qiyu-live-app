package org.qiyu.live.api.vo;

/**
 * @Author QingY
 * @Date: Created in 14:50 2024/4/20
 * @Description
 */
public class UserLoginVO {

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserLoginVO{" +
                "userId=" + userId +
                '}';
    }
}
