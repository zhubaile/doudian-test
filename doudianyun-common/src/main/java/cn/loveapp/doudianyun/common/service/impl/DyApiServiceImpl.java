package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.service.DyApiService;
import cn.loveapp.doudianyun.common.service.RedisService;
import cn.loveapp.doudianyun.common.util.OpenApiUtil;
import com.doudian.open.api.compass_CommonProductQueryData.CompassCommonProductQueryDataRequest;
import com.doudian.open.api.compass_CommonProductQueryData.CompassCommonProductQueryDataResponse;
import com.doudian.open.api.compass_CommonProductQueryData.param.CompassCommonProductQueryDataParam;
import com.doudian.open.api.compass_getProductSaleData.CompassGetProductSaleDataRequest;
import com.doudian.open.api.compass_getProductSaleData.CompassGetProductSaleDataResponse;
import com.doudian.open.api.compass_getProductSaleData.param.CompassGetProductSaleDataParam;
import com.doudian.open.api.product_listV2.ProductListV2Request;
import com.doudian.open.api.product_listV2.ProductListV2Response;
import com.doudian.open.api.product_listV2.param.ProductListV2Param;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailRequest;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailResponse;
import com.doudian.open.api.product_qualityDetail.param.ProductQualityDetailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

@Service
@Slf4j
public class DyApiServiceImpl implements DyApiService {

    @Autowired
    private RedisService redisService;

    @Override
    public ProductListV2Response getItemList(Long status, Long page, Long size) {
        ProductListV2Request request = new ProductListV2Request();
        ProductListV2Param param = request.getParam();
        param.setStatus(status);
        param.setCheckStatus(3L);
//        param.setProductType(0L);
//        param.setStartTime(1619161933L);
//        param.setEndTime(1619162000L);
//        param.setUpdateStartTime(1619161933L);
//        param.setUpdateEndTime(1619161933L);
        param.setPage(page);
        param.setSize(size);
//        param.setStoreId(123456L);
//        param.setName("标题");
//        param.setProductId(3600137140018749665);
        ProductListV2Response response = OpenApiUtil.invokeOpenApi("4463798", request);
        System.out.println("获取商品列表--" + response + "---获取商品列表");
        return response;
    }

    @Override
    public CompassGetProductSaleDataResponse GetProductSaleData(Integer dateType, List<String> productIds) {
        // 获取当前日期
        SimpleDateFormat endDate = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = endDate.format(new Date());

        CompassGetProductSaleDataRequest request = new CompassGetProductSaleDataRequest();
        CompassGetProductSaleDataParam param = request.getParam();
        param.setDateType(dateType);
        param.setProductIds(productIds);
        param.setEndDate(formattedDate);
        CompassGetProductSaleDataResponse response = OpenApiUtil.invokeOpenApi("4463798", request);

        System.out.println("获取商品销量曝光点击--" + response + "---获取商品销量曝光点击");
//        CompassGetProductSaleDataResponse response = request.execute(accessToken);
        return response;
    }

    /**
     * 商品信息质量分查询API
     * @return
     */
    @Override
    public ProductQualityDetailResponse getQualityDetail(Long productId) {
        ProductQualityDetailRequest request = new ProductQualityDetailRequest();
        ProductQualityDetailParam param = request.getParam();
        param.setProductId(productId);
        ProductQualityDetailResponse response = OpenApiUtil.invokeOpenApi("4463798", request);
        System.out.println("商品信息质量分查询API--" + response + "---商品信息质量分查询API");
        return response;
    }

    @Override
    public CompassCommonProductQueryDataResponse getCommonProductQueryData(String productId) {
        // 获取前一天的日期时间
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String yesterdayStr = yesterday.format(DateTimeFormatter.ofPattern("yyyy/MM/dd 00:00:00"));
        System.out.println("前一天日期时间：" + yesterdayStr);

        // 获取前三十天的日期时间
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        String thirtyDaysAgoStr = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy/MM/dd 00:00:00"));
        System.out.println("前三十天日期时间：" + thirtyDaysAgoStr);

        CompassCommonProductQueryDataRequest request = new CompassCommonProductQueryDataRequest();
        CompassCommonProductQueryDataParam param = request.getParam();
        param.setEndDate(yesterdayStr);
        param.setDateType(23);
        param.setActivityId("");
        param.setBeginDate(thirtyDaysAgoStr);
        param.setProductId(productId);
        param.setPageNo(1L);
        param.setPageSize(50L);
        param.setSortedField("product_show_ucnt");
        param.setIsAsc(false);
        CompassCommonProductQueryDataResponse response = OpenApiUtil.invokeOpenApi("4463798", request);
        System.out.println("获取商品搜索词--" + response + "---获取商品搜索词");
        return response;
    }

    @Override
    public Object getTopSearchQueryPredict(String categoryId) {

        // ******设置一天自动过期

        String hotSearchWord = (String) redisService.redisHashGet("cid_hot_search_word", categoryId);
        if (hotSearchWord.isEmpty()) {
            // 从接口获取
//            TradeTopSearchQueryPredictRequest request = new TradeTopSearchQueryPredictRequest();
//            TradeTopSearchQueryPredictParam param = request.getParam();
//            param.setCategoryId(categoryId);
//            CompassCommonProductQueryDataResponse response = OpenApiUtil.invokeOpenApi("4463798", request);

            // 获取的数据存入redis
//            List<String> strList = Arrays.asList("apple", "banana", "orange");
//            String hotSearchWordStr = String.join(",", strList);
//            redisService.redisHashSet("cid_hot_search_word", categoryId, hotSearchWordStr);
//            return strList;
        }
        String[] strArray = hotSearchWord.split(",");
        List<String> strList = Arrays.asList(strArray);

//        TradeTopSearchQueryPredictRequest request = new TradeTopSearchQueryPredictRequest();
//        TradeTopSearchQueryPredictParam param = request.getParam();
//        param.setCategoryId(25108L);
        return strList;
    }
}
