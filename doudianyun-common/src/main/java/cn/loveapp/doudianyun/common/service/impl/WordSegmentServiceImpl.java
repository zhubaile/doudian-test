package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.service.WordSegmentService;
import cn.loveapp.doudianyun.common.util.RegexUtil;
import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhongzijie
 * @Date: 2023/3/22 17:01
 * @Description: 分词service实现类
 */
@Service
@Slf4j
public class WordSegmentServiceImpl implements WordSegmentService {

    @Autowired
    private JiebaSegmenter jiebaSegmenter;

    @Override
    public List<String> wordSegment(String text, boolean ignoreNumber, boolean ignoreSingleWord, List<String> exclusionKeywords, boolean useFuzzyMatch) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        List<String> segments = jiebaSegmenter.sentenceProcess(text);
        if (BooleanUtils.isTrue(ignoreSingleWord)) {
            segments = segments.stream().filter((e) -> e.trim().length() > 1).collect(Collectors.toList());
        }
        if (BooleanUtils.isTrue(ignoreNumber)) {
            segments = segments.stream().filter((e) -> !RegexUtil.isInteger(e.trim())).collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(exclusionKeywords)) {
            if (useFuzzyMatch) {
                segments = segments.stream().filter((e) -> !fuzzyMatch(exclusionKeywords, e)).collect(Collectors.toList());
            } else {
                segments = segments.stream().filter((e) -> !exclusionKeywords.contains(e)).collect(Collectors.toList());
            }
        }
        return segments;
    }

    /**
     * 模糊匹配，判断keyword是否包含exclusionKeywords当中的元素
     * @param exclusionKeywords
     * @param keyword
     * @return
     */
    private boolean fuzzyMatch(List<String> exclusionKeywords, String keyword) {
        for (String exclusionKeyword : exclusionKeywords) {
            if (keyword.contains(exclusionKeyword)) {
                return true;
            }
        }
        return false;
    }
}
