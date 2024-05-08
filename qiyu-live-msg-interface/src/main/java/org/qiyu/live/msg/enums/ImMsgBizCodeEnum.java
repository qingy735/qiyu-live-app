package org.qiyu.live.msg.enums;

/**
 * @Author idea
 * @Date: Created in 22:47 2023/7/14
 * @Description
 */
public enum ImMsgBizCodeEnum {

    LIVING_ROOM_IM_CHAT_MSG_BIZ(5555, "直播间im聊天消息");

    int code;
    String desc;

    ImMsgBizCodeEnum(int code, String desc) {
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
