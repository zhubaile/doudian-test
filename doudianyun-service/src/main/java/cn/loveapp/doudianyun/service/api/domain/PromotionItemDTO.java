package cn.loveapp.doudianyun.service.api.domain;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @Description: 促销词表 - promotion - 实体类
 */
@Data
public class PromotionItemDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 促销热词
     */
    private String wordName;

    /**
     * 转化率
     */
    private BigDecimal winnerv;

    /**
     * 点击率
     */
    private BigDecimal clickv;

    /**
     * 搜索量
     */
    private Long countv;

    /**
     * 竞争度
     */
    private Long impressionv;

    /**
     * 备用
     */
    private String remark;

    /**
     * 0-未删除 1-已删除
     */
    private Integer isDel;
}
