package cn.loveapp.doudianyun.db.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品列表数据存储
 */
@Data
public class ItemDetectionList implements Serializable {
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
    private String productId;

    /**
     * 主图
     */
    private String img;

    /**
     * 商品类型；0-普通；1-新客商品；3-虚拟；6-玉石闪购；7-云闪购 ；127-其他类型；
     */
    private Integer productType;

    /**
     * 商品审核状态: 1-未提交；2-待审核；3-审核通过；4-审核未通过；5-封禁；7-审核通过待上架
     */
    private Integer checkStatus;

    /**
     * 标题的检测类型 123-对应-优良差
     */
    private Integer titleType;

    /**
     * 标题检测结果
     */
    private String detectionResult;

    /**
     * 外部数据-曝光访问量和销量
     */
    private String externalData;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 商品更新时间
     */
    private Date updateTime;
}
