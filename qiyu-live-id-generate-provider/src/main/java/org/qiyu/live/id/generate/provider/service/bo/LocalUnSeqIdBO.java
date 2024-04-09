package org.qiyu.live.id.generate.provider.service.bo;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: QingY
 * @Date: Created in 15:16 2024-04-09
 * @Description:
 */
public class LocalUnSeqIdBO {
    // mysql配置的id
    private Integer id;
    // 对应分布式id的配置说明
    private String desc;
    // 链表记录id值
    private ConcurrentLinkedQueue<Long> idQueue;
    // 本地内存记录id段的开始位置
    private Long currentStart;
    // 本地内存记录id段的结束位置
    private Long nextThreshold;
    // 步长
    private Integer step;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ConcurrentLinkedQueue<Long> getIdQueue() {
        return idQueue;
    }

    public void setIdQueue(ConcurrentLinkedQueue<Long> idQueue) {
        this.idQueue = idQueue;
    }

    public Long getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(Long currentStart) {
        this.currentStart = currentStart;
    }

    public Long getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(Long nextThreshold) {
        this.nextThreshold = nextThreshold;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "LocalUnSeqIdBO{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", idQueue=" + idQueue +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                ", step=" + step +
                '}';
    }
}
