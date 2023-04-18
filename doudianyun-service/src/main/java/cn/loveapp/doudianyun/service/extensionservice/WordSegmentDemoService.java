package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.service.WordSegmentService;
import com.alibaba.fastjson.JSONArray;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.*;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/12 15:04
 * @Description: 示例 - 使用jieba分词
 */
@ExtensionService("wordSegment")
@Slf4j
public class WordSegmentDemoService implements ExtensionServiceHandler<Object, List<String>> {

    @Autowired
    private WordSegmentService wordSegmentService;

    @Override
    public BaseResponse<List<String>> handle(BaseRequest<Object> req) {
        String text = "大码高级感韩版卫衣男士";
        Object data = req.getData();
        if (data != null) {
            Map<String, Object> map = (Map<String, Object>) data;
            String title = (String) map.get("title");

            text = data.toString();
        }

        List<String> words = wordSegmentService.wordSegment(text, false, false, null, false);
        log.info("分词前: {}", text);
        log.info("分词后: {}", JSONArray.toJSONString(words));

        BaseResponse<List<String>> rsp = BaseResponse.<List<String>>builder()
                .success(true)
                .data(words)
                .build();
        return rsp;
    }
}
