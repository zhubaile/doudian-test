package cn.loveapp.doudianyun.service.api.request;

import lombok.Data;

@Data
public class GetAllItemListRequest {
    /**
     * 页码
     */
    private Long page;

    /**
     * 页数
     */
    private Long size;

    /**
     * 0-在线；1-下线；2-删除；
     */
    private Long status;

}
