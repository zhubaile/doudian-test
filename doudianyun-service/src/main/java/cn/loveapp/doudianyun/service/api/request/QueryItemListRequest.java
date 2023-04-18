package cn.loveapp.doudianyun.service.api.request;

import lombok.Data;

@Data
public class QueryItemListRequest {
    /**
     * 页码
     */
    private Integer page;

    /**
     * 页数
     */
    private Integer size;

    /**
     * 0-在线；1-下线；2-删除；
     */
    private Integer status;

    /**
     * 123--优良差
     */
    private Integer titleType;

    /**
     * 审核状态  1-未提交；2-待审核；3-审核通过；4-审核未通过；5-封禁；7-审核通过待上架
     */
    private Integer checkStatus;
}
