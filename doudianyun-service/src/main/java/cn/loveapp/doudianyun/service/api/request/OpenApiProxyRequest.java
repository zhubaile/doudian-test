package cn.loveapp.doudianyun.service.api.request;

import lombok.Data;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/13 18:38
 * @Description: 抖店api代理转发请求实体类
 */
@Data
public class OpenApiProxyRequest {

    /**
     * 请求的抖店api url后缀
     */
    private String url;

    /**
     * 请求体 JSON格式
     */
    private String body;
}
