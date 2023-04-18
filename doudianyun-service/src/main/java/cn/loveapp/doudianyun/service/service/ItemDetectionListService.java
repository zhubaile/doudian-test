package cn.loveapp.doudianyun.service.service;


import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;

import java.util.List;
import java.util.Map;

public interface ItemDetectionListService {

    /**
     * 插入数据
     */
    Void addItemDetectionList(ItemDetectionList singleItemList); // PromotionItemResponse PromotionItemRequest request

    /**
     * 更新数据
     */
    Void updateItemDetectionList(ItemDetectionList singleItemList);

    /**
     * 查询
     */
    List<ItemDetectionList> getItemDetectionList(ItemDetectionList singleItemList, int page, int size);

    /**
     * 获取查询的总数
     */
    int getItemDetectionListTotal(ItemDetectionList singleItemList);

    /**
     * 查询
     */
    Boolean getIsHasItem(String userId, String productId);
}