package org.qiyu.live.framework.web.starter.limit;

import java.lang.annotation.*;

/**
 * @Author: QingY
 * @Date: Created in 15:15 2024-05-15
 * @Description:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 允许请求的量
     *
     * @return
     */
    int limit();

    /**
     * 限流时长
     *
     * @return
     */
    int second();

    /**
     * 提示内容
     *
     * @return
     */
    String msg() default "请求过于频繁";

}
