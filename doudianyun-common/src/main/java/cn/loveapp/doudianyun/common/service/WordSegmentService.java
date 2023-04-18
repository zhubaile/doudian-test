package cn.loveapp.doudianyun.common.service;

import java.util.List;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/12 13:58
 * @Description: 分词service
 */
public interface WordSegmentService {

    /**
     * 获取text的分词结果
     *
     * @param text
     * @param ignoreNumber 分词结果是否忽略数字
     * @param ignoreSingleWord 分词结果是否忽略单个字符
     * @param exclusionKeywords 分词结果需要屏蔽的词语
     * @param useFuzzyMatch 过滤关键词时是否启用模糊匹配
     * @return
     */
    List<String> wordSegment(String text, boolean ignoreNumber, boolean ignoreSingleWord, List<String> exclusionKeywords, boolean useFuzzyMatch);
}
