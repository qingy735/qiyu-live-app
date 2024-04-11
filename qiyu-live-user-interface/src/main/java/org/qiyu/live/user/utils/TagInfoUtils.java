package org.qiyu.live.user.utils;

/**
 * @Author: QingY
 * @Date: Created in 22:38 2024-04-10
 * @Description:
 */
public class TagInfoUtils {

    /**
     * 判断是否存在某个标签
     *
     * @param tagInfo  用户当前标签值
     * @param matchTag 被查询是否匹配的标签值
     * @return
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag > 0 && ((tagInfo & matchTag) == matchTag);
    }
}
