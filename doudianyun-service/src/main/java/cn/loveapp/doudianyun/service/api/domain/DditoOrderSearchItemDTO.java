package cn.loveapp.doudianyun.service.api.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 10:58
 * @Description:
 */
@Data
public class DditoOrderSearchItemDTO {

    /**
     * 用户名称
     */
    private String nick;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品模型名称
     */
    private String articleItemName;

    /**
     * 订购周期
     */
    private String orderCycle;

    /**
     * 订购时间
     */
    private Date createDate;

    /**
     * 订购周期开始时间
     */
    private Date orderCycleStart;

    /**
     * 订购周期结束时间
     */
    private Date orderCycleEnd;

    /**
     * 实付金额
     */
    private Long totalPayFee;

    /**
     * 支付方式--1：微信，2：支付宝，7：该订单为试用订单，0元单
     */
    private Integer payType;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 子订单号
     */
    private String bizOrderId;

    /**
     * 1 生效中 2 已失效 3 已停用 4 未付款
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 手机号
     */
    private String phone;
}
