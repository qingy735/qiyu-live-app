package org.qiyu.live.living.interfaces.rpc;


import org.qiyu.live.common.interfaces.dto.PageWrapper;
import org.qiyu.live.living.interfaces.dto.LivingRoomReqDTO;
import org.qiyu.live.living.interfaces.dto.LivingRoomRespDTO;

import java.util.List;

/**
 * @Author idea
 * @Date: Created in 21:20 2023/7/19
 * @Description
 */
public interface ILivingRoomRpc {

    /**
     * 支持根据roomId查询出批量的userId（set）存储，3000个人，元素非常多，O(n)
     *
     * @param livingRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 查询所有的直播间类型
     *
     * @param type
     * @return
     */
    List<LivingRoomRespDTO> listAllLivingRoomFromDB(Integer type);

    /**
     * 直播间列表的分页查询
     *
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据用户id查询是否正在开播
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * 开启直播间
     *
     * @param livingRoomReqDTO
     * @return
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);


    /**
     * 关闭直播间
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);
}
