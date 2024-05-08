package org.qiyu.live.im.constants;

/**
 * @Author: QingY
 * @Date: Created in 21:40 2024-04-28
 * @Description:
 */
public enum AppIdEnum {
    QIYU_LIVE_BIZ(1, "旗鱼直播业务");
    int code;
    String desc;

    AppIdEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
