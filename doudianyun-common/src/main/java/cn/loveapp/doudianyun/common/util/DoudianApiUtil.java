package cn.loveapp.doudianyun.common.util;

import cn.loveapp.doudianyun.common.dto.PlatformApiDTO;
import com.doudian.open.core.DoudianOpRequest;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/13 15:26
 * @Description: 抖店API协助类
 */
@Slf4j
public class DoudianApiUtil {

    private static Map<String, Class<? extends DoudianOpRequest>> mapForDoudian = new HashMap<>();

    static {
        try {
            loadMapForDoudian(mapForDoudian, getDoudianAPIRequests());
        } catch (Exception e) {
            log.error("初始化抖店接口代理工具类失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取对应的请求实体类型
     *
     * @param method
     * @return
     */
    public static Class<? extends DoudianOpRequest> getRequestClass(String method) {
        return mapForDoudian.get(method);
    }

    /**
     * 加载抖店api-map
     *
     * @param map
     * @param requestSet
     * @throws Exception
     */
    private static void loadMapForDoudian(Map<String, Class<? extends DoudianOpRequest>> map, Set<Class<? extends DoudianOpRequest>> requestSet) throws Exception {
        for (Class clazz : requestSet) {
            PlatformApiDTO apiForDoudianDTO = convertApiForDoudianDTO(clazz);
            map.put(apiForDoudianDTO.getMethod(), apiForDoudianDTO.getRequestClass());
        }
    }

    /**
     * 解析抖店请求实体类
     *
     * @param clazz
     * @return
     * @throws Exception
     */
    private static PlatformApiDTO convertApiForDoudianDTO(Class<? extends DoudianOpRequest> clazz) throws Exception {
        PlatformApiDTO apiForDoudianDTO = new PlatformApiDTO();
        DoudianOpRequest requestInstance = clazz.newInstance();
        apiForDoudianDTO.setMethod(requestInstance.getUrlPath());
        apiForDoudianDTO.setRequestClass(clazz);
        return apiForDoudianDTO;
    }

    /**
     * 反射获取所有抖店api请求实体类
     *
     * @return
     */
    private static Set<Class<? extends DoudianOpRequest>> getDoudianAPIRequests() {
        Reflections reflections = new Reflections("com.doudian.open.api");
        Set<Class<? extends DoudianOpRequest>> requestSet = reflections.getSubTypesOf(DoudianOpRequest.class);
        return requestSet;
    }
}
