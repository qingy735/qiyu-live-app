package org.qiyu.live.user.constants;

/**
 * @Author: QingY
 * @Date: Created in 13:57 2024-04-11
 * @Description:
 */
public enum CacheAsyncDeleteCode {

    USER_INFO_DELETE(0, "用户基础信息删除"),
    USER_TAG_DELETE(1, "用户标签删除");

    int code;
    String desc;

    CacheAsyncDeleteCode(int code, String desc) {
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
