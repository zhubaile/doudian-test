package cn.loveapp.doudianyun.common.service.impl;

import cn.loveapp.doudianyun.common.constant.ActiveProfileConstant;
import cn.loveapp.doudianyun.common.service.GetCommonWordService;
import cn.loveapp.doudianyun.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetCommonWordServiceImpl implements GetCommonWordService {

    @Autowired
    private RedisService redisService;

    @Override
    public String[] getBadWords() {
        String badWordsData = (String) redisService.redisHashGet("aiyong", "badWords");
        if (badWordsData.isEmpty()) {
            badWordsData = ActiveProfileConstant.BAT_WORDS;
            redisService.redisHashSet("aiyong", "badWords", badWordsData);
        }
        String[] badWords = badWordsData.split("、");
        return badWords;
    }

    @Override
    public String[] getPromoWords() {
        String promotionWordsData = (String) redisService.redisHashGet("aiyong", "promotionWords");
        if (promotionWordsData.isEmpty()) {
            promotionWordsData = ActiveProfileConstant.PROMO_WORDS;
            redisService.redisHashSet("aiyong", "promotionWords", promotionWordsData);
        }
        String[] promoWords = promotionWordsData.split("、");
        return promoWords;
    }
}
