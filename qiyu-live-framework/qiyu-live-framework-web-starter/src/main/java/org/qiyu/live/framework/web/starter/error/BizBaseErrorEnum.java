package org.qiyu.live.framework.web.starter.error;

/**
 * @Author: QingY
 * @Date: Created in 22:08 2024-05-11
 * @Description:
 */
public enum BizBaseErrorEnum implements QiyuBaseError {
    PARAM_ERROR(100001, "参数异常"),
    TOKEN_ERROR(100002, "用户token异常");

    BizBaseErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;
    private String errorMsg;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
