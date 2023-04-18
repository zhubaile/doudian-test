package cn.loveapp.doudianyun.db.common.mapper;

import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDetectionListMapper {

    /**
     * 获取商品信息是否存在
     */
    Boolean getIsHasItem(@Param("userId") String userId, @Param("productId") String productId);

    /**
     * 查询商品列表数据
     */
    List<ItemDetectionList> getDetectionList(ItemDetectionList params, int begin, int size);

    /**
     * 查询商品列表数据总数
     */
    int getDetectionListTotal(ItemDetectionList params);

    /**
     * 插入商品列表数据
     */
    void interDetectionList(ItemDetectionList params);

    /**
     * 更新商品列表数据
     */
    void updateDetectionList(ItemDetectionList params);
}
