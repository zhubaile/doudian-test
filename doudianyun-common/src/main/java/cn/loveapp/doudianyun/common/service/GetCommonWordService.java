package cn.loveapp.doudianyun.common.service;

import java.util.List;
import java.util.Map;

public interface GetCommonWordService {
    /**
     * 获取违规词
     */
    String[] getBadWords();

    /**
     * 获取促销词
     */
    String[] getPromoWords();
}
