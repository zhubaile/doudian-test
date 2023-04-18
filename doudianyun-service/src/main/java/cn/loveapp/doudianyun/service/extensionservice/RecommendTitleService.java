package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.service.*;
import cn.loveapp.doudianyun.service.api.request.DetailCheckTitleRequest;
import cn.loveapp.doudianyun.service.api.request.RecommendTitleRequest;
import cn.loveapp.doudianyun.service.api.response.DetailCheckTitleResponse;
import cn.loveapp.doudianyun.service.api.response.RecommendTitleResponse;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@ExtensionService("recommendTitleService.get")
@Slf4j
public class RecommendTitleService implements ExtensionServiceHandler<RecommendTitleRequest, RecommendTitleResponse> {

    @Autowired
    private DetectionTitleService detectionTitleService; // 标题优化公共方法

    @Autowired
    private DyApiService dyApiService; // sdk

    @Autowired
    private WordSegmentService wordSegmentService; // 分词

    @Autowired
    private RedisService redisService;

    @Autowired
    private GetCommonWordService getCommonWordService;

    @Override
    public BaseResponse<RecommendTitleResponse> handle(BaseRequest<RecommendTitleRequest> req) {
        String modifiedTitle = "";
        String productId = "";
        int optimizeNum = 1; // 1=首次优化 2=重新优化
        // 获取参数
        RecommendTitleRequest data = req.getData();
        if (data != null) {
            modifiedTitle = data.getTitle();
            productId = data.getProductId();
            optimizeNum = data.getOptimizeNum() == 1 ? 1 : 2;
        }
        // 返回内容
        RecommendTitleResponse recommendTitleData = new RecommendTitleResponse();
        if (modifiedTitle.isEmpty() || productId.isEmpty()) {
            BaseResponse<RecommendTitleResponse> rsp = BaseResponse.<RecommendTitleResponse>builder()
                    .success(true)
                    .code("40004")
                    .message("缺少必填参数")
                    .data(recommendTitleData)
                    .build();
            return rsp;
        }
        // 创建检测结果对象
        Map<String, Object> titleResult = new HashMap<String, Object>();
        titleResult.put("oldTitle", modifiedTitle); // 优化后的标题
        titleResult.put("newTitle", modifiedTitle); // 优化后的标题

        String newTitle = modifiedTitle; // 新标题
        StringBuilder modifyContent = new StringBuilder(); // 修改的展示数据

        // 1 去除违规词
        // 1.1 获取违规词数据
        String[] badWords = getCommonWordService.getBadWords();
//        String badWordsData = (String) redisService.redisHashGet("aiyong", "badWords");
//        String[] badWords = badWordsData.split("、");
        // 记录存在的违规词
        StringBuilder hasBadWordBuilder = new StringBuilder();
        // 1.2 删除违规词
        for (String word : badWords) {
            if (newTitle.contains(word)) {
                hasBadWordBuilder.append(word).append('、');
                newTitle = newTitle.replace(word, "");
            }
        }
        String hasBadWord = hasBadWordBuilder.toString();
        // 1.3 删除违规词+记录修改内容
        if (hasBadWord.length() > 0) {
            hasBadWord = hasBadWord.substring(0, hasBadWord.length() - 1); // 删掉多余的、
            modifyContent.append("；删除了违禁词：").append(hasBadWord);
        }

        // 2 获取商品销量 进行校验 **
        final int itemSales = 2;
        if (itemSales >= 10) {
            titleResult.put("newTitle", newTitle); // 优化后的标题
            titleResult.put("modifyContent", modifyContent.toString()); // 修改的内容记录
            recommendTitleData.setTitleResult(titleResult);
            BaseResponse<RecommendTitleResponse> rsp = BaseResponse.<RecommendTitleResponse>builder()
                    .success(true)
                    .code("10000")
                    .message("获取新标题成功")
                    .data(recommendTitleData)
                    .build();
            return rsp;
        }

        // 3 删除重复词
        // 3.1 获取重复词语
        String repeatWorld = detectionTitleService.topCheckMoreKey(newTitle);
        // 3.2 删除重复词语
        if (!repeatWorld.isEmpty()) {
            String[] repeatWorldArr = repeatWorld.split(",");
            Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]+"); // 预编译正则表达式
            for (String value : repeatWorldArr) {
                // 判断重复词是否含有汉字 如果是数字和英文 那就先放弃吧
                if (!chinesePattern.matcher(value).find()) {
                    continue;
                }
                // 删除重复词
                int curIndex = newTitle.indexOf(value);
                if (curIndex >= 0) {
                    newTitle = newTitle.substring(0, curIndex) + newTitle.substring(curIndex + value.length());
                }
            }
            modifyContent.append("；删除了重复词：").append(repeatWorld);
        }

        // 4 删除特殊字符
        final Map<String, String> specialChar = detectionTitleService.hasSpecialCharacters(newTitle, "del");
        String specialCharStr = specialChar.get("specialStr"); // 获取删除的字符
        newTitle = specialChar.get("curStr"); // 获取删除后的标题
        if (specialCharStr.length() > 0) {
            // 存在特殊字符
            modifyContent.append("；删除了特殊字符：").append(specialCharStr);
        }
        titleResult.put("newTitle", newTitle); // 优化后的标题
        titleResult.put("modifyContent", modifyContent.toString()); // 修改的内容记录

        // 5 该删除的词全部删完了，接下来添加词
        // 获取促销词
        String[] promoWords = getCommonWordService.getPromoWords();
//        String promotionWordsData = (String) redisService.redisHashGet("aiyong", "promotionWords");
//        String[] promoWords = promotionWordsData.split("、");
        // 获取热搜词
        String[] hotWords = { "热搜词", "强哥", "懂姐", "你好" };

        // 优化前的标题长度
        int oldTitleLen = detectionTitleService.getTitleLength(newTitle);
        List<String> replaceOldWords = new ArrayList<>(); // 被替换的词
        List<String> replaceNewWords = new ArrayList<>(); // 替换的词 跟上面的值一一对应

        // 验证原标题中是否存在对应词组
        boolean isIncludeHotWords = false; // 标题包含的热搜词
        // 虚拟的标题，将即将被删除的词删除掉 防止出现重复记录需要删除的词
        String virtualTitle = newTitle;
        for (String hotVal : hotWords) {
            if (virtualTitle.contains(hotVal)) {
                // 标题中含有类目热搜词
                isIncludeHotWords = true;
                // 进行替换 首次优化存在热搜词不进行替换
                if (optimizeNum == 1) {
                    // 存在排名最高热搜词了
                    break;
                }
                // 重新优化了，随机替换一下热搜词吧
                String curWordVal = detectionTitleService.getNeedWord(optimizeNum, "hotWord", hotWords, virtualTitle, replaceNewWords, 1);
                if (!curWordVal.isEmpty()) {
                    replaceOldWords.add(hotVal); // 被替换的词
                    replaceNewWords.add(curWordVal);
                    modifyContent.append("；删除了热搜词：").append(hotVal).append("；添加了热搜词：").append(curWordVal);
                    virtualTitle = virtualTitle.replace(hotVal, "");
                }
                if (optimizeNum == 2) {
                    // 都重新优化了，就跳出循环
                    break;
                }
            }
        }
        if (!isIncludeHotWords) {
            // 标题中不存在热搜词，需要添加一个热搜词
            String curWordVal = detectionTitleService.getNeedWord(optimizeNum, "hotWord", hotWords, newTitle, replaceNewWords, 1);
            if (!curWordVal.isEmpty()) {
                replaceNewWords.add(curWordVal);
                modifyContent.append("；添加了热搜词：").append(curWordVal);
            }
        }

        /*
         * 将所有被替换的词和要加入的词做统计 计算一下字符 看看超没超过限制
         * 超过的话需要分词删除一些，不超过就直接添加，继续循环添加热搜、促销、属性词
         */
        String replaceOldWordStr = String.join("", replaceOldWords);
        String replaceNewWordStr = String.join("", replaceNewWords);

        // 获取最新标题的字符长度
        int newTitleLen = oldTitleLen - detectionTitleService.getTitleLength(replaceOldWordStr) + detectionTitleService.getTitleLength(replaceNewWordStr);
        if (newTitleLen > 60) {
            // 加完词字符数超了，需要删词了
            int canRemoveNum = newTitleLen - 60; // 最少需要删除的字符
            // 删除可删除的分词
            Map<String, String> delData = detectionTitleService.deleteKeyWord(newTitle, canRemoveNum, replaceOldWords);
            newTitle = delData.get("title"); // 删除过后的标题
            String delVal = delData.get("delVal"); // 删除的词
            if (!delVal.isEmpty()) {
                modifyContent.append("；删除的词：").append(delVal);
            }
            newTitleLen = detectionTitleService.getTitleLength(newTitle) - detectionTitleService.getTitleLength(replaceOldWordStr) + detectionTitleService.getTitleLength(replaceNewWordStr);
        }
        if (newTitleLen < 57) {
            // 字符数小于57，还有加词的空间，继续循环添加热搜促销和属性词
            int needAddStrLen = 60 - newTitleLen; // 需要添加的字符

System.out.println(newTitle + "----" + needAddStrLen);

            replaceNewWords = detectionTitleService.addSurplusKeyWord(replaceNewWords, newTitle, needAddStrLen, hotWords, promoWords, modifyContent);

        }
        // 进行关键词的替换
        for (int i = 0; i < replaceOldWords.size(); i++) {
            String val = replaceOldWords.get(i);
            String newWord = replaceNewWords.get(i);
            newTitle = newTitle.substring(0, newTitle.indexOf(val)) + newWord + newTitle.substring(newTitle.indexOf(val) + val.length());
        }
        // 拿到需要纯添加的词
        List<String> addWords = replaceNewWords.subList(replaceOldWords.size(), replaceNewWords.size());
        String addWordStr = String.join("", addWords); // 转成字符串

        // 对标题进行分词
        List<String> participlesWords = wordSegmentService.wordSegment(newTitle, false, false, null, false);
        String finallyTitle = ""; // 新标题
        String[] titleArr = newTitle.split(participlesWords.get(0), 2);
        finallyTitle = participlesWords.get(0) + addWordStr + titleArr[1];

        // 返回优化记录
        titleResult.put("newTitle", finallyTitle); // 优化后的标题
        titleResult.put("modifyContent", modifyContent.toString()); // 修改的内容记录
        recommendTitleData.setTitleResult(titleResult);
        BaseResponse<RecommendTitleResponse> rsp = BaseResponse.<RecommendTitleResponse>builder()
                .success(true)
                .code("10000")
                .message("获取新标题成功")
                .data(recommendTitleData)
                .build();
        return rsp;
    }
}