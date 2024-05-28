package org.qiyu.live.framework.web.starter.error;

/**
 * @Author: QingY
 * @Date: Created in 22:13 2024-05-11
 * @Description:
 */
public class ErrorAssert {

    /**
     * 判断参数不能为空
     *
     * @param obj
     * @param qiyuBaseError
     */
    public static void isNotNull(Object obj, QiyuBaseError qiyuBaseError) {
        if (obj == null) {
            throw new QiyuErrorException(qiyuBaseError.getErrorCode(), qiyuBaseError.getErrorMsg());
        }
    }

    /**
     * 判断字符串不为空
     *
     * @param str
     * @param qiyuBaseError
     */
    public static void inNotBlank(String str, QiyuBaseError qiyuBaseError) {
        if (str == null || str.trim().length() == 0) {
            throw new QiyuErrorException(qiyuBaseError.getErrorCode(), qiyuBaseError.getErrorMsg());
        }
    }

    /**
     * flag == true
     *
     * @param flag
     * @param qiyuBaseError
     */
    public static void isTrue(boolean flag, QiyuBaseError qiyuBaseError) {
        if (!flag) {
            throw new QiyuErrorException(qiyuBaseError.getErrorCode(), qiyuBaseError.getErrorMsg());
        }
    }

    /**
     * flag == true
     *
     * @param flag
     * @param qiyuErrorException
     */
    public static void isTrue(boolean flag, QiyuErrorException qiyuErrorException) {
        if (!flag) {
            throw qiyuErrorException;
        }
    }

}
