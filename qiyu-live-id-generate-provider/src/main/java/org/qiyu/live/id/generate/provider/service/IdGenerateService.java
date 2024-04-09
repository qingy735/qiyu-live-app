package org.qiyu.live.id.generate.provider.service;

public interface IdGenerateService {

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
