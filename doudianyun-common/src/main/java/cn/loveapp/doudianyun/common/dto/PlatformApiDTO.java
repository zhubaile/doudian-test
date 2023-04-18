package cn.loveapp.doudianyun.common.dto;

import lombok.Data;

/**
 * @Author: zhongzijie
 * @Date: 2021/12/10 17:23
 * @Description: 解析平台SDK请求实体类获取关键元数据
 */
@Data
public class PlatformApiDTO {

    /**
     * 全平台通用，接口名
     */
    private String method;

    /**
     * 全平台通用，请求实体类
     */
    private Class requestClass;
}
