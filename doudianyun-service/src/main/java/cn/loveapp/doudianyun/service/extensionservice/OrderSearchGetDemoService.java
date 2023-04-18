package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.service.api.request.DditoOrderSearchItemGetRequest;
import cn.loveapp.doudianyun.service.api.request.PromotionItemRequest;
import cn.loveapp.doudianyun.service.api.response.DditoOrderSearchItemGetResponse;
import cn.loveapp.doudianyun.service.api.response.PromotionItemResponse;
import cn.loveapp.doudianyun.service.service.TestService;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 14:59
 * @Description: 示例 - 访问数据库查询订购记录表
 * @Description: 示例 - 访问redis
 */
@ExtensionService("dditoOrderSearchItem.get")
@Slf4j
public class OrderSearchGetDemoService implements ExtensionServiceHandler<DditoOrderSearchItemGetRequest, DditoOrderSearchItemGetResponse> {

    @Autowired
    private TestService testService;

    /**
     * 如果要存取字符串数据，用stringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 如果要存取对象数据（复杂数据），用redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public BaseResponse<DditoOrderSearchItemGetResponse> handle(BaseRequest<DditoOrderSearchItemGetRequest> req) {
        // 往redis存一个key为testredis、value为hello的string类型数据，并设置10分钟的超时时间
//        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
//        opsForValue.set("testredis", "hello", 10, TimeUnit.MINUTES);
//        // 读取testredis的值
//        String testredis = opsForValue.get("testredis");
//        log.info("testredis: " + testredis);

        // 往redis存一个hash类型数据
        HashOperations hashOperations = redisTemplate.opsForHash();
//        hashOperations.put("test:redis", "k1", "v1");
//        hashOperations.put("test:redis", "k2", "v2");


//        String badWords = "国家级、世界级、最高级、全网、最佳、最大、第一、唯一、首个、首选、最好、精确、顶级、最高、最低、最具、最便宜、最先进、最大程度、最新技术、最先进科学、国家级产品、填补国内空白、独家、首家、第一品牌、金牌、名牌、优秀、资深、最赚、超赚、最先、巨星、著名、奢侈、至尊、王者、顶级享受、给他最好的、全网销量第一、全球首发、全国首家、全网首发、世界领先、顶级工艺、最新科学、最先进加工工艺、最时尚、极品、顶尖、终极、最受欢迎、王牌、**之王、NO.1、Top1、极致、永久、掌门人、领袖品牌、独一无二、绝无仅有、前无古人、史无前例、万能、驰名、销量+冠军、抄底、极端、空前绝后、绝对、巅峰、顶峰、中国+名牌、真正、最舒适、最真实、最流行、最贴心、极佳、最美、最火、最前沿、最合适、最实用、最值得、最高性价比、最满意、最全面、最优、第一选择、全能冠军、完美、无语伦比、销量最大、最超值、最环保、最具保暖性、最具人气、最美味、最前端、最实在、最贴身、最突出、最用心、最优秀、最新、最轻便、最逼真、最牛、最热卖、最热销、最强、最有效、最实惠、最专业、最安全、最新鲜 、最新发明、最萌、世界之最、最牢固、最伟大、最健康第一、第1、唯1、惟一、销量冠军、NO1、领导品牌、世界品牌、大品牌之一、100%回头客、100%瘦身、百分百、百分之百、100%、国际品质、驰名商标、中国名牌、CCTV 、央视品牌、秒杀全网 、淘宝之冠、淘宝之王、前所未有、top、无法超越、独有、国际一流、著名商标、特价、工厂价、直销价、出厂价、最爱、最奢侈、最低级、最低价、最聚拢、最符合、最先享受、最后一波、中国第一、排名第一、全国第一、仅此一次、仅此一款、全球级、宇宙级、绝佳、独家配方、首款、全国销量冠军、国家领导人、中国驰名、大牌、遥遥领先、领导者、缔造者、创领品牌、领先上市、领袖、冠军、祖传、特效、无敌、纯天然、高档、精准、特供、专供、专家推荐、质量免检、无需国家质量检测、免抽检、领导人推荐、机关推荐、秒杀、抢爆、再不抢就没了、不会更便宜了、错过就没机会了、万人疯抢、全民疯抢、全民抢购、抢疯了、卖疯了、随时结束、随时涨价、马上降价、国际最、最高端、最底、史上最低价、最后、全网第一、销量第一、number1、TOP1、遗留、仅此一次仅此一款、全国X大品牌之一、销冠、国际级、千万级、百万级、星级、甲级、超甲级、尖端、高级、致极、极具、至臻、臻品、臻致、臻席、压轴、问鼎、空前、绝后、绝版、无双、非此莫属、无人能及、鼎级、鼎冠、定鼎、翘楚之作、不可再生、寸土寸金、淋漓尽致、无与伦比、卓越、卓著、前无古人后无来者、珍稀、臻稀、稀少、绝不在有、稀世珍宝、千金难求、世所罕见、不可多得、寥寥无几、屈指可数、独创、独据、开发者、创始者、发明者、首发、首席、首府、首屈一指、领导人、国门、国宅、首次、旺铺、黄金价值、黄金地段、金钱、之王、王者楼王、墅王、皇家、引领、创领、领航、耀领、全民免单、点击有惊喜、点击获取、点击转身、抽奖、售罄、售空、不会再便宜、错过不再、错过即无、未曾有过、抢购、免费领、帝都、皇城、皇室领地、皇室、皇族、殿堂、白宫、王府、府邸、皇室住所、政府、行政大楼、贵族、高贵、隐贵、上流、层峰、富人区、名门、阶层、阶级、升值价值、价值洼地、价值天成、千亿价值、投资回报、众筹、抄涨、升值潜力无限、买到即赚到、倒计时、仅限、周年庆、特惠趴、闪购、国旗、国徽、国歌、一流、淫秽、色情、赌博、迷信、恐怖、暴力、种族、宗教、歧视、贱、草泥马、傻逼、诅咒、屎、正品、专柜、清仓、无可挑剔、亏本、无可比拟、装逼、正版、限时、超级、万分、绝不反弹、助孕、百年品质、彻底消除、永不复发、根除、神效、逢凶化吉、药妆、医学护肤品、EGF、中国驰名商标、老字号、机关专供、点击翻转、点击试穿、恭喜获奖、点击领奖、国家免检、全国首发、最高档、TOP.1、时尚最低价、最新科技、一天、假一赔百、假一赔厂、假一罚万、假一罚命、假一关店、假货关厂、生精、最、爆款、热销、批发";
//        hashOperations.put("aiyong", "badWords", badWords);

//        String promotionWords = "NEW、新款上市、新款、优质、精品、疯狂促销、甩卖、热销推荐、HOT、好评如潮、巨划算、实拍、冲钻、热卖、满就送、人气、人民的宝贝、新品、特惠、新品推荐、惊爆、限时折扣、掌柜推荐、势力周、镇店之宝、推荐、震撼低价、真品";
//        hashOperations.put("aiyong", "promotionWords", promotionWords);
        // 读取hash类型数据
        Object promotionWordsss = hashOperations.get("aiyong", "promotionWords");
        log.info("test:redis promotionWords: " + promotionWordsss);


//        PromotionItemRequest www = ;
        String asd = testService.promotionItemInter();
        log.info("test:redis asd: " + asd);

        DditoOrderSearchItemGetRequest request = req.getData();
        DditoOrderSearchItemGetResponse response = testService.dditoOrderSearchItemGet(request);
        BaseResponse<DditoOrderSearchItemGetResponse> rsp = BaseResponse.<DditoOrderSearchItemGetResponse>builder()
                .success(true)
                .data(response)
                .build();
        return rsp;
    }
}
