package org.qiyu.live.id.generate.interfaces;

/**
 * @Author: QingY
 * @Date: Created in 14:53 2024-04-09
 * @Description:
 */
public interface IdGenerateRpc {

    /**
     * 获取有序ID
     *
     * @param id
     * @return
     */
    Long getSeqId(Integer id);

    /**
     * 获取无序ID
     *
     * @param id
     * @return
     */
    Long getUnSeqId(Integer id);
}
