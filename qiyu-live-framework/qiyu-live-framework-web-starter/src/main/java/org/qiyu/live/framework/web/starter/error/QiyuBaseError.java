package org.qiyu.live.framework.web.starter.error;

/**
 * @Author: QingY
 * @Date: Created in 22:07 2024-05-11
 * @Description:
 */
public interface QiyuBaseError {

    /**
     * 定义返回的错误代码
     *
     * @return
     */
    int getErrorCode();

    /**
     * 定义返回的错误提示语
     *
     * @return
     */
    String getErrorMsg();

}
