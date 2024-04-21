package org.qiyu.live.common.interfaces.enums;

/**
 * @Author: QingY
 * @Date: Created in 18:28 2024-04-21
 * @Description:
 */
public enum GatewayHeaderEnum {

    USER_LOGIN_ID("用户id", "qiyu_gh_user_id");

    String desc;
    String name;

    GatewayHeaderEnum(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
