package org.qiyu.live.living.interfaces.constants;

/**
 * @Author idea
 * @Date: Created in 15:49 2023/7/25
 * @Description
 */
public enum LivingRoomTypeEnum {

    DEFAULT_LIVING_ROOM(1, "普通直播间"),
    PK_LIVING_ROOM(2, "普通直播间");

    LivingRoomTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
