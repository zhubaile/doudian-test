package cn.loveapp.doudianyun.common.service;

import com.doudian.open.api.product_listV2.data.CategoryDetail;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/12 13:58
 * @Description: 分词service
 */
public interface DetectionTitleService {

    /**
     * 根据分数获取标题的优良差
     * @param score 分数
     **/
    String getTitleResults(int score);

    /**
     * 检测是否含有重复词
     * @param str 待检测字符串
     * @return 重复词组成的字符串，用逗号隔开
     */
    String topCheckMoreKey(String str);

    /**
     * 验证当前字符是否包含词组中的某一项
     * @param title 字符
     * @param curWords 当前词组
     * @return 是否包含词组中的某一项
     */
    boolean getIncludeWord(String title, String[] curWords);

    /**
     * 验证是否包含特殊字符
     * @param str 当前要验证的字符
     * @param status get 获取包含的特殊字符 del删除特殊字符
     * @return 包含特殊字符
     */
    Map<String, String> hasSpecialCharacters (String str, String status);

    /**
     * 获取字符长度
     * @param str 字符
     * @return 字符长度
     */
    int getTitleLength(String str);

    /**
     * 获取随机数 数据量超过10 就获取0-10
     */
    int getRandNum(int min, int max, boolean hasMax);

    /**
     * 中文字符拆成数组
     */
    List<String> mb_str_split(String str, int split_length);

    /**
     * 获取词组分词后的数据
     * allLength 字符总长度
     * strArr 字符拆成数组的数据
     * needStrLen 取多少长度的字符
     * allKeyWords 存储所有词的数据
     */
    List<String> getProcessingWord(int allLength, String[] strArr, int needStrLen, List<String> allKeyWords);

    /**
     * 获取这个词是否能使用
     * @param curWord 当前检测的词
     * @param title 标题
     * @param existenceWord 已经存在的词
     */
    boolean getWordStatus(String curWord, String title, List<String> existenceWord);

    /**
     * 取出来需要添加的词
     * type 1取数据最高的 2随机取
     * status 值的类型
     * keyWordArr 取值的数据源
     * title 当前标题
     * existenceWord 已经存在将要添加的词
     * index 当前执行次数
     */
    String getNeedWord(int type, String status, String[] keyWordArr, String title, List<String> existenceWord, int index);

    /**
     删除分词
     @param title 标题
     @param canRemoveNum 需要删除多少个字符
     @param notRemove 不能删除的词
     @return String 返回删除后的标题
     */
    Map<String, String> deleteKeyWord(String title, int canRemoveNum, List<String> notRemove);

    /**
     单纯的词不够，随机补充词就好了
     hasWorks 存在的词
     title 标题
     needAddStrLen 需要添加的字符数量
     hotSearchWords 热搜词数据源
     promotionWords 促销词数据源
     */
    List<String> addSurplusKeyWord(List<String> hasWorks, String title, int needAddStrLen, String[] hotSearchWords, String[] promotionWords, StringBuilder modifyContent);

    /**
     * 单个加词逻辑
     */
    Map<String, Object> addSurplusSingleKeyWord(String[] data, String title, List<String> hasWorks, int needAddStrLen);

    /**
     * 获取类目信息
     */
    Map<String, String> getCategoryDetail(CategoryDetail data);

}
