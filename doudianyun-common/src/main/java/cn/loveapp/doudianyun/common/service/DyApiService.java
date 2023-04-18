package cn.loveapp.doudianyun.common.service;

import com.doudian.open.api.compass_CommonProductQueryData.CompassCommonProductQueryDataResponse;
import com.doudian.open.api.compass_getProductSaleData.CompassGetProductSaleDataResponse;
import com.doudian.open.api.product_listV2.ProductListV2Response;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailResponse;

import java.util.List;
import java.util.Map;

public interface DyApiService {

    /**
     * 获取商品列表
     */
    ProductListV2Response getItemList (Long status, Long page, Long size);

    /**
     * 获取商品销量曝光成交等数据
     */
    CompassGetProductSaleDataResponse GetProductSaleData (Integer dateType, List<String> productIds);

    /**
     * 商品信息质量分查询API
     */
    ProductQualityDetailResponse getQualityDetail (Long productId);

    /**
     * 获取商品搜索词
     */
    CompassCommonProductQueryDataResponse getCommonProductQueryData (String productId);

    /**
     * 获取类目热搜词
     */
    Object getTopSearchQueryPredict (String categoryId);

}
