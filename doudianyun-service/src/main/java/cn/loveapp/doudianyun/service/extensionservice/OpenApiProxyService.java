package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.util.DoudianApiUtil;
import cn.loveapp.doudianyun.common.util.OpenApiUtil;
import cn.loveapp.doudianyun.service.api.request.OpenApiProxyRequest;
import cn.loveapp.doudianyun.service.api.response.OrderDetailGetResponse;
import com.alibaba.fastjson.JSONObject;
import com.doudian.open.core.DoudianOpRequest;
import com.doudian.open.core.DoudianOpResponse;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/13 18:31
 * @Description: 抖店api代理服务
 */
@ExtensionService("openApiProxy")
@Slf4j
public class OpenApiProxyService implements ExtensionServiceHandler<OpenApiProxyRequest, String> {
    @Override
    public BaseResponse<String> handle(BaseRequest<OpenApiProxyRequest> req) {
        OpenApiProxyRequest openApiProxyRequest = req.getData();
        if (openApiProxyRequest == null) {
            BaseResponse<String> rsp = BaseResponse.<String>builder()
                    .success(false)
                    .message("请求业务参数不能为空")
                    .build();
            return rsp;
        }
        String url = openApiProxyRequest.getUrl();
        String body = openApiProxyRequest.getBody();
        Class<? extends DoudianOpRequest> requestClass = DoudianApiUtil.getRequestClass(url);
        DoudianOpRequest request = JSONObject.parseObject(body, requestClass);
        Object response = OpenApiUtil.invokeOpenApi(req.getAuthId(), request);
        Class<? extends DoudianOpResponse<?>> responseClass = request.getResponseClass();
        String responseStr = JSONObject.toJSONString(response);
        BaseResponse<String> rsp = BaseResponse.<String>builder()
                .success(true)
                .data(responseStr)
                .build();
        return rsp;
    }
}
