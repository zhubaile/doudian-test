package cn.loveapp.doudianyun.common.config;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/12 13:37
 * @Description: jieba分词器
 */
@Component
public class JiebaAnalysisConfig {

    @Bean
    public JiebaSegmenter jiebaSegmenter() {
        Path path = Paths.get(new File( getClass().getClassLoader().getResource("dicts/user.dict").getPath() ).getAbsolutePath() ) ;
        WordDictionary.getInstance().loadUserDict(path) ;
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        return jiebaSegmenter;
    }
}
