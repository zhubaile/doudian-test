package cn.loveapp.doudianyun.db.common.mapper;

import cn.loveapp.doudianyun.db.common.entity.PromotionItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PromotionMapper {
    /**
     * 插入热搜词
     */
    String interPromotion(@Param("remark") String remark);

    /**
     * 获取促销词
     */
    String queryPromotion(@Param("remark") String remark);
}
