package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.service.DetectionTitleService;
import cn.loveapp.doudianyun.common.service.WordSegmentService;
import com.doudian.open.api.product_listV2.data.CategoryDetail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DetectionTitleServiceImpl implements DetectionTitleService {

    @Autowired
    private WordSegmentService wordSegmentService; // 分词

    @Override
    public String getTitleResults(int score) {
        if (score >= 90) {
            return "优";
        } else if (60 < score && score < 90) {
            return "良";
        }
        return "差";
    }

    @Override
    public String topCheckMoreKey(String str) {
        char[] charArr = str.toCharArray();
        int len = charArr.length;
        StringBuilder moreStr = new StringBuilder();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (charArr[j] == charArr[i] && j - i <= len / 2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(charArr[j]);
                    for (int k = j + 1, m = i + 1; k < len && m < j; k++, m++) {
                        if (charArr[k] == charArr[m]) {
                            sb.append(charArr[k]);
                        } else {
                            break;
                        }
                    }
                    String str1 = sb.toString();
                    if (str1.equals(String.valueOf(charArr[j]))) {
                        // 重复的字符只有一个
                    } else if (moreStr.length() == 0) {
                        moreStr.append(str1);
                    } else if (!moreStr.toString().contains(str1)) {
                        moreStr.append(",").append(str1);
                    }
                }
            }
        }
        return moreStr.toString();
    }

    @Override
    public boolean getIncludeWord(String title, String[] curWords) {
        if (curWords == null || curWords.length == 0) {
            // 检测的数据为空，那就不进行验证
            return true;
        }
        for (String word : curWords) {
            if (title.contains(word)) {
                // 标题存在当前词组的数据
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, String> hasSpecialCharacters(String str, String status) {
        Map<String, String> specialCharData = new HashMap<String, String>();
        String[] specialCharacters = {"\\\\", "/", "~", "!", "@", "#", "\\$", "%", "\\^", "&", "\\*", "\\(", "\\)", "（", "）", "_", "\\+", "\\{", "\\}", ":", "<", ">", "\\?", "\\[", "\\]", ",", "\\.", ";", "'", "`", "-", "=", "\\|", "\\s+"};
        Pattern pattern = Pattern.compile(String.join("|", specialCharacters));
        Matcher matcher = pattern.matcher(str);
        StringBuffer curStr = new StringBuffer();
        StringBuilder delStr = new StringBuilder();
        while (matcher.find()) {
            // 记录存在的特殊字符
            delStr.append(matcher.group());
            if (status.equals("del")) {
                matcher.appendReplacement(curStr, "");
            }
        }
        if (status.equals("del")) {
            matcher.appendTail(curStr);
        }
        specialCharData.put("specialStr", delStr.toString()); // 存在的特殊字符
        specialCharData.put("curStr", curStr.toString()); // 修改后的字符
        return specialCharData;
    }

    @Override
    public int getTitleLength(String str) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < 0x80) {
                len += 1;
            } else {
                len += 2;
            }
        }
        return len;
    }

    @Override
    public int getRandNum(int min, int max, boolean hasMax) {
        int curMax = max > 10 ? 10 : max; // 正常情况下，随机数都在0-10之间
        int maxNum = hasMax ? max : curMax; // hasMax为true的情况，直接使用传进来的最大值就行了
        return (int) (Math.random() * (maxNum - min + 1)) + min;
    }

    @Override
    public List<String> mb_str_split(String str, int split_length) {
        if (split_length < 1) {
            return null;
        }
        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> arr = new ArrayList<>();
        int len = str.length();
        for (int i = 0; i < len; i += split_length) {
            if (i + split_length < len) {
                arr.add(str.substring(i, i + split_length));
            } else {
                arr.add(str.substring(i));
            }
        }
        return arr;
    }

    @Override
    public List<String> getProcessingWord(int allLength, String[] strArr, int needStrLen, List<String> allKeyWords) {
        int startStrLen = 0;

        while (startStrLen + needStrLen <= allLength) {
            String[] curVal = Arrays.copyOfRange(strArr, startStrLen, startStrLen + needStrLen); // 当前分词的数据
            String curStrVal = String.join("", curVal); // 当前分词val
            allKeyWords.add(curStrVal);
            startStrLen++;
        }
        --needStrLen;
        if (needStrLen > 1) {
            return getProcessingWord(allLength, strArr, needStrLen, allKeyWords);
        }
        return allKeyWords;
    }

    @Override
    public boolean getWordStatus(String curWord, String title, List<String> existenceWord) {
        String existenceStr = String.join("", existenceWord); // 转换成字符串，不用全部包含，含有重复词就不要
        boolean stop = false;
        int allLength = getTitleLength(curWord);
        if (allLength <= 2) {
            if (title.contains(curWord) || existenceStr.contains(curWord)) {
                return false;
            }
        } else {
            List<String> strSplitArr = mb_str_split(curWord, 1);

            String[] strArr = strSplitArr.toArray(new String[0]);

            List<String> allKeyWords = new ArrayList<>();
            getProcessingWord(strArr.length, strArr, strArr.length, allKeyWords);
            for (String val : allKeyWords) {
                if (title.contains(val) || existenceStr.contains(val)) {
                    // 词重复了
                    stop = true;
                    break;
                }
            }
        }
        // 重复词-含有周岁、销售的词和字符小于2的词都不要
        if (stop || curWord.contains("周岁") || curWord.contains("销售") || getTitleLength(curWord) < 2) {
            return false;
        }

        Map<String, String> specialChar = hasSpecialCharacters(curWord, "get");
        String specialCharStr = specialChar.get("specialStr"); // 获取特殊字符
        if (specialCharStr.length() > 0) {
            // 存在特殊字符
            return false;
        }

        // 验证是否含有中文
        Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(curWord);
        return matcher.find();
    }

    @Override
    public String getNeedWord(int type, String status, String[] keyWordArr, String title, List<String> existenceWord, int index) {
        String curWord = "";
        if (index == 10) {
            return curWord;
        }
        if (type == 1) {
            curWord = keyWordArr[0];
        } else {
            boolean hasMax = status.equals("proWord");
            int randNum = getRandNum(0, keyWordArr.length - 1, hasMax);
            System.out.println("randNum-----" + randNum + "-----randNum");

            curWord = keyWordArr[randNum];
        }
        boolean isWord = getWordStatus(curWord, title, existenceWord);
        if (!isWord) {
            return getNeedWord(2, status, keyWordArr, title, existenceWord, ++index);
        }
        return curWord;
    }

    @Override
    public Map<String, String> deleteKeyWord(String title, int canRemoveNum, List<String> notRemove) {
        Map<String, String> resolverArr = new HashMap<String, String>();

        List<String> removeArr = new ArrayList<String>(); // 可以删除的词
        int removeArrLen = 0;

        // 先对标题进行分词
        List<String> participles = wordSegmentService.wordSegment(title, false, false, null, false);

        // 第一组词保留 所以 $i > 0, i==0 的时候为第一组词
        for (int i = participles.size() - 1; i > 0; i--) {
            // 按序循环，判断剔除词性
            // 需要满足如下几个条件：1. 不在query_words中 2.如果只有一个字
            int not_remove = 0; // 此词需要移除
            String curVal = participles.get(i); // 当前分词的值
            if (getTitleLength(curVal) == 1) {
                // 只有一个字
                continue;
            }
            // 需要替换的词不能删除的
            for (String hasVal : notRemove) {
                if (hasVal.contains(curVal)) {
                    // 该分词不能删除
                    not_remove = 1;
                    break;
                }
            }

            if (not_remove != 1) {
                // 该分词可以移除
                removeArr.add(curVal); // 需要删除的分词
                removeArrLen += getTitleLength(curVal); // 删除的字符长度
                if (removeArrLen >= canRemoveNum) {
                    // 删除的字符长度 >= 需要删除的词  结束
                    break;
                }
            }
        }
        for (String delVal : removeArr) {
            // 删除可忽略的名词
            title = title.replace(delVal, "");
        }
        String delValStr = String.join("、", removeArr);
        resolverArr.put("title", title);
        resolverArr.put("delVal", delValStr);
        return resolverArr;
    }

    @Override
    public List<String> addSurplusKeyWord(List<String> hasWorks, String title, int needAddStrLen, String[] hotSearchWords, String[] promotionWords, StringBuilder modifyContent) {
        int oldStrLen = needAddStrLen;
        // 添加热搜词
        Map<String, Object> hotSearchWordsResult = addSurplusSingleKeyWord(hotSearchWords, title, hasWorks, needAddStrLen);
        hasWorks = (List<String>) hotSearchWordsResult.get("hasWorks");
        needAddStrLen = (int) hotSearchWordsResult.get("needAddStrLen");

        String addHotWord = (String) hotSearchWordsResult.get("addWord");

        System.out.println(addHotWord + "---addHotWord");

        if (!addHotWord.isEmpty()) {
            // 添加了热搜词
            modifyContent.append("；添加了热搜词：").append(addHotWord);
        }

        if ((boolean) hotSearchWordsResult.get("stop")) {
            return hasWorks;
        }
        // 添加促销词
        Map<String, Object> promotionWordsResult = addSurplusSingleKeyWord(promotionWords, title, hasWorks, needAddStrLen);
        hasWorks = (List<String>) promotionWordsResult.get("hasWorks");
        needAddStrLen = (int) promotionWordsResult.get("needAddStrLen");
        String addPromotionWord = (String) promotionWordsResult.get("addWord");
        if (!addPromotionWord.isEmpty()) {
            modifyContent.append("；添加了促销词：").append(addPromotionWord);
            // 添加了促销词 我知道了不知道是否新款上市重复啊我好的裤子@$%最好的裤子!我
        }
        if ((boolean) promotionWordsResult.get("stop")) {
            return hasWorks;
        }
        if (oldStrLen == needAddStrLen) {
            // 添加完一轮结果字符长度没变，那就退出吧
            return hasWorks;
        }
        // 一轮过后词还不够，再来一遍
        return addSurplusKeyWord(hasWorks, title, needAddStrLen, hotSearchWords, promotionWords, modifyContent);
    }

    @Override
    public Map<String, Object> addSurplusSingleKeyWord(String[] data, String title, List<String> hasWorks, int needAddStrLen) {
        Map<String, Object> resultMap = new HashMap<>();
        for (String hotVal : data) {
            int curLen = getTitleLength(hotVal);
            // 获取这个词能否使用
            boolean curState = getWordStatus(hotVal, title, hasWorks);
            if (curState && curLen <= needAddStrLen) {
                // 符合要求 加入
                hasWorks.add(hotVal);
                resultMap.put("addWord", hotVal);
                needAddStrLen -= curLen;
                break;
            }
        }
        // 兼容一下没有拿到添加词的情况
        if (!resultMap.containsKey("addWord")) {
            resultMap.put("addWord", "");
        }
        if (needAddStrLen <= 3) {
            // 说明标题已经有57以上的字符了，可以停止了
            resultMap.put("hasWorks", hasWorks);
            resultMap.put("stop", true);
            resultMap.put("needAddStrLen", needAddStrLen);
            return resultMap;
        }
        resultMap.put("hasWorks", hasWorks);
        resultMap.put("stop", false);
        resultMap.put("needAddStrLen", needAddStrLen);
        return resultMap;
    }

    @Override
    public Map<String, String> getCategoryDetail(CategoryDetail data) {
        Long cid = 0L;
        StringBuilder cidStr = new StringBuilder();
        StringBuilder cidName = new StringBuilder();
        Map<String, String> categoryMap = new HashMap<>();

        if (!data.getFirstCname().isEmpty()) {
            cid = data.getFirstCid();
            cidName.append(data.getFirstCname());
            cidStr.append(cid);
        }

        if (!data.getSecondCname().isEmpty()) {
            cid = data.getSecondCid();
            cidName.append(">>").append(data.getSecondCname());
            cidStr.append(">>").append(cid);
        }

        if (!data.getThirdCname().isEmpty()) {
            cid = data.getThirdCid();
            cidName.append(">>").append(data.getThirdCname());
            cidStr.append(">>").append(cid);
        }

        if (!data.getFourthCname().isEmpty()) {
            cid = data.getFourthCid();
            cidName.append(">>").append(data.getFourthCname());
            cidStr.append(">>").append(cid);
        }
        categoryMap.put("cid", String.valueOf(cid));
        categoryMap.put("cidName", cidName.toString());
        categoryMap.put("cidStr", cidStr.toString());
        return categoryMap;
    }
}
