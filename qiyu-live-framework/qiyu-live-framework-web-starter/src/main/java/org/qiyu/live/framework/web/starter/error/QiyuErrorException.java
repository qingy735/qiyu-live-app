package org.qiyu.live.framework.web.starter.error;

/**
 * @Author: QingY
 * @Date: Created in 22:05 2024-05-11
 * @Description:
 */
public class QiyuErrorException extends RuntimeException {
    private int errorCode;
    private String errorMsg;

    public QiyuErrorException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
