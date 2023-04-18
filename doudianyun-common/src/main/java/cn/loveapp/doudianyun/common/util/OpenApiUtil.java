package cn.loveapp.doudianyun.common.util;

import cn.loveapp.doudianyun.common.constant.ActiveProfileConstant;
import com.doudian.open.core.DoudianOpRequest;
import com.jinritemai.cloud.base.core.util.AuthThreadLocalUtil;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 17:19
 * @Description: 抖店api调用工具类
 */
public class OpenApiUtil {

    /**
     * 调用抖店api
     *
     * @param shopId
     * @param request
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R invokeOpenApi(String shopId, DoudianOpRequest<T> request) {
        if (ActiveProfileConstant.ACTIVE_PROFILE.equals(ActiveProfileConstant.PROFILE_DEV)) {
            AuthThreadLocalUtil.set(shopId);
        }
        R execute = request.execute();
        return execute;
    }
}
