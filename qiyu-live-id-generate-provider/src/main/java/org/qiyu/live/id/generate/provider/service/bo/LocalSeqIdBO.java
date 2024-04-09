package org.qiyu.live.id.generate.provider.service.bo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: QingY
 * @Date: Created in 15:16 2024-04-09
 * @Description:
 */
public class LocalSeqIdBO {
    // mysql配置的id
    private Integer id;
    // 对应分布式id的配置说明
    private String desc;
    // 当前在本地内存的id值
    private AtomicLong currentValue;
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

    public AtomicLong getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(AtomicLong currentValue) {
        this.currentValue = currentValue;
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
        return "LocalSeqIdBO{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", currentValue=" + currentValue +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                ", step=" + step +
                '}';
    }
}
