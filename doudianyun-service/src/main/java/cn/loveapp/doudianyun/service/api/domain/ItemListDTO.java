package cn.loveapp.doudianyun.service.api.domain;

import lombok.Data;

@Data
public class ItemListDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String nick;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品在店铺中状态: 0-在线；1-下线；2-删除
     */
    private Integer status;

    /**
     * 子类目id
     */
    private Integer cid;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品id
     */
    private String product_id;

    /**
     * 主图
     */
    private String img;

    /**
     * 商品类型；0-普通；1-新客商品；3-虚拟；6-玉石闪购；7-云闪购 ；127-其他类型；
     */
    private Integer product_type;

    /**
     * 商品审核状态: 1-未提交；2-待审核；3-审核通过；4-审核未通过；5-封禁；7-审核通过待上架
     */
    private Integer check_status;

    /**
     * 标题的检测类型 123-对应-优良差
     */
    private Integer title_type;

    /**
     * 标题检测结果
     */
    private String detection_result;

    /**
     * 外部数据-曝光访问量和销量
     */
    private String external_data;

    /**
     * 创建时间
     */
    private String gmt_create;

    /**
     * 修改时间
     */
    private String gmt_modified;

    /**
     * 商品更新时间
     */
    private String update_time;
}
