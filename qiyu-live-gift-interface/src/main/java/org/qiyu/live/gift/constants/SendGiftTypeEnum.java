package org.qiyu.live.gift.constants;

/**
 * @Author: QingY
 * @Date: Created in 19:31 2024-05-21
 * @Description:
 */
public enum SendGiftTypeEnum {
    DEFAULT_SEND_GIFT(0, "直播间默认送礼物"),
    PK_SEND_GIFT(1, "直播间PK送礼物");

    SendGiftTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    Integer code;
    String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
