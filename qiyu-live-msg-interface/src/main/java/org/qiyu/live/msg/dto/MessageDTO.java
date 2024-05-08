package org.qiyu.live.msg.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 发送消息的内容
 *
 * @Author idea
 * @Date: Created in 15:00 2023/7/11
 * @Description
 */
public class MessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8982006120358366161L;
    private Long userId;
    private Long objectId;
    /**
     * 消息类型
     */
    private Integer type;
    /**
     * 消息内容
     */
    private String content;
    private Date createTime;
    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
