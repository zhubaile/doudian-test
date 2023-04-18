package cn.loveapp.doudianyun.common.service;

import java.util.Map;

public interface DetailCheckTitleService {

    /**
     * 获取标题检测结果
     **/
    Map<String, Object> getDetailCheckTitle(String title, String productId, int cid);

}
