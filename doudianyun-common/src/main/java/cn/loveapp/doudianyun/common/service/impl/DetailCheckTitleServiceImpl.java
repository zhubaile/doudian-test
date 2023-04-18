package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.service.*;
import com.doudian.open.api.compass_CommonProductQueryData.CompassCommonProductQueryDataResponse;
import com.doudian.open.api.compass_getProductSaleData.CompassGetProductSaleDataResponse;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailResponse;
import com.doudian.open.api.product_qualityDetail.data.FieldProblemItem;
import com.doudian.open.api.product_qualityDetail.data.ProductQualityDetailData;
import com.jinritemai.cloud.base.api.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DetailCheckTitleServiceImpl implements DetailCheckTitleService {

    @Autowired
    private DetectionTitleService detectionTitleService;

    @Autowired
    private DyApiService dyApiService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private GetCommonWordService getCommonWordService;

    @Override
    public Map<String, Object> getDetailCheckTitle(String title, String productId, int cid) {
        try {
//            String title = "";
//            String productId = "";
//            String cid = "";
//            DetailCheckTitleRequest data = req.getData();
//            if (data != null) {
//                title = data.getTitle();
//                productId = data.getProductId();
//                cid = data.getCid();
//            }

            // 返回内容
//            DetailCheckTitleResponse detailCheckTitleData = new DetailCheckTitleResponse();

            // 获取参数
            System.out.println("title--" + title + "---title");

            // 创建检测结果对象
            Map<String, Object> titleResult = new HashMap<String, Object>();
            titleResult.put("offWord", ""); // 存在的违规词
            titleResult.put("specialWord", ""); // 存在的特殊字符
            titleResult.put("moreWord", ""); // 存在的重复词
            titleResult.put("officialPrompt", ""); // 官方校验的问题
            titleResult.put("hasHotWord", true); // 是否存在热搜词
            titleResult.put("hasPromoWord", true); // 是否存在促销词
            titleResult.put("titleLength", 0); // 标题长度
            titleResult.put("score", 100); // 检测结果分数
            titleResult.put("titleState", "优"); // 检测结果对应状态
            titleResult.put("titleType", 1); // 123-对应-优良差
            titleResult.put("errorNum", 0); // 几项数据有问题
            titleResult.put("externalData", ""); // 外部数据曝光点击等数据
            //

            // test
            if (title.isEmpty() || productId.isEmpty()) {
                System.out.println("没有拿到标题--" + title + "---title");
                return titleResult;
            }

            // 获取促销词、违规词
            String[] badWords = getCommonWordService.getBadWords();
            String[] promoWords = getCommonWordService.getPromoWords();

            // 检测是否含有违规词
            String fiveStr = "";
            for (String badWord : badWords) {
                if (title.contains(badWord)) {
                    fiveStr += badWord + "、";
                    titleResult.put("offWord", fiveStr);
                }
            }
            if (!fiveStr.isEmpty()) {
                // 存在违规词
                titleResult.put("score", (int) titleResult.get("score") - 40); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                titleResult.put("offWord", fiveStr.substring(0, fiveStr.length() - 1));
            }

            // 获取商品销量 ** productId
            List<String> productIds = new ArrayList<>(); // Collections.singleton(productId)
            productIds.add("3610914185900364341");
            productIds.add("3610914169718757254");
            productIds.add("3607521077934308802");

            System.out.println("productIds--" + productIds + "---productIds");
            CompassGetProductSaleDataResponse productSaleData = dyApiService.GetProductSaleData(23, productIds);

            // /compass/CommonProductQueryData 拿到销量 曝光 点击 这个数据确认一下是在任务分发的时候获取还是检测时候获取
            // externalData
            // 23 一个月
            final int itemSales = 2;

            // 进行标题字符检测
            int strLen = detectionTitleService.getTitleLength(title);
            titleResult.put("titleLength", strLen); // 标题长度
            if (strLen < 50) {
                // 字符长度大于30扣5分，小于30 销量超10的扣10分，小于10单的扣15分
                int removeScore = strLen > 30 ? 5 : (itemSales > 10 ? 10 : 15);
                titleResult.put("score", (int) titleResult.get("score") - removeScore); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
            }

            // 通过接口/product/qualityDetail获取到商品有"field_name": "标题"的情况，扣30分
            // 如果存在含有标题的问题项目扣30分 **
            ProductQualityDetailResponse titleQualityData = dyApiService.getQualityDetail(Long.valueOf(productId));
            if (Objects.equals(titleQualityData.getCode(), "10000")) {
                StringBuilder officialPromptBuilder = new StringBuilder();
                // 请求成功
                ProductQualityDetailData titleQualityDetail = titleQualityData.getData();
                List<FieldProblemItem> problemList =  titleQualityDetail.getFieldProblem();
                for (FieldProblemItem problemItem: problemList) {
                    if (problemItem.getFieldKey().equals("product_name")) {
                        // 存在标题的问题,进行记录
                        officialPromptBuilder.append(problemItem.getSuggestion()).append(";");
                    }
                }
                String newOfficialPrompt = officialPromptBuilder.toString();
                if (!newOfficialPrompt.isEmpty()) {
                    titleResult.put("score", (int) titleResult.get("score") - 30); // 检测结果分数
                    titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                    titleResult.put("officialPrompt", newOfficialPrompt); // 官方检测出的问题
                }
            }

            // 销量超10的话，检测结束
            if (itemSales > 10) {
                return titleResult;
            }

            // 继续检测剩下的项目
            // 检测特殊字符
            final Map<String, String> specialChar = detectionTitleService.hasSpecialCharacters(title, "get");
            String specialCharStr = specialChar.get("specialStr");
            if (!specialCharStr.isEmpty()) {
                // 存在特殊字符
                titleResult.put("score", (int) titleResult.get("score") - 5); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                titleResult.put("specialWord", specialCharStr);
            }
            // 检测重复词
            final String moreKey = detectionTitleService.topCheckMoreKey(title);
            if (!moreKey.isEmpty()) {
                // 存在重复词
                titleResult.put("score", (int) titleResult.get("score") - 5); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                titleResult.put("moreWord", moreKey);
            }

            // 验证是否存在促销词
            boolean isPromoWords = detectionTitleService.getIncludeWord(title, promoWords);

            System.out.println("isPromoWords--" + isPromoWords + "---isPromoWords");

            if (!isPromoWords) {
                // 不存在促销词
                titleResult.put("score", (int) titleResult.get("score") - 5); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                titleResult.put("hasPromoWord", false); // 表示没有促销词
            }

            // 获取热搜词 **
            // 获取搜索词
            CompassCommonProductQueryDataResponse productQueryData = dyApiService.getCommonProductQueryData(productId);
            System.out.println("productQueryData--" + productQueryData + "---productQueryData");
            // 方案一 /compass/CommonProductQueryData 直接调用接口获取数据，看看标题中是否存在热搜词（这个是已经存在的热搜词数据）
            // 方案一肯定要做，主要看能不能方案二省略掉
            // 记录搜索词的数据

            if (productQueryData.getCode().equals("10000") && productQueryData.getData().getProductList().isEmpty()) { // 不存在搜索词
                // 验证是否存在热搜词
            }

            // 方案二 /trade/TopSearchQueryPredict 通过类目id的形式获取，然后以类目id为key进行存储
            String[] hotWords = { "热搜词", "热搜", "11" };
            // 验证是否存在热搜词
            boolean isHotWords = detectionTitleService.getIncludeWord(title, hotWords);
            if (!isHotWords) {
                // 不存在热搜词
                titleResult.put("score", (int) titleResult.get("score") - 20); // 检测结果分数
                titleResult.put("errorNum", (int) titleResult.get("errorNum") + 1); // 错误项
                titleResult.put("hasHotWord", false); // 表示没有热搜词
            }
            // 分数对应优良差
            final String curTitleState = detectionTitleService.getTitleResults((int) titleResult.get("score"));
            titleResult.put("titleState", curTitleState); // 标题结果
            titleResult.put("titleType", curTitleState.equals("差") ? 3 : (curTitleState.equals("良") ? 2 : 1)); // 标题结果

            // 返回全部检测结果
            return titleResult;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
