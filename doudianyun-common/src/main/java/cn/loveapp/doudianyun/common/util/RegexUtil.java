package cn.loveapp.doudianyun.common.util;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/12 14:53
 * @Description: 正则表达式工具类
 */
public class RegexUtil {

    /**
     * 数字正则表达式
     */
    private static final String NUMBER_PATTERN = "-?[0-9]+.?[0-9]*";

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(NUMBER_PATTERN);
    }
}
