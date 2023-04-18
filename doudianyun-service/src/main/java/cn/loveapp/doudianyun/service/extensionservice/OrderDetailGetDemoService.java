package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.util.OpenApiUtil;
import cn.loveapp.doudianyun.service.api.request.OrderDetailGetRequest;
import cn.loveapp.doudianyun.service.api.response.OrderDetailGetResponse;
import com.alibaba.fastjson.JSONObject;
//import com.doudian.open.api.order_orderDetail.OrderOrderDetailRequest;
//import com.doudian.open.api.order_orderDetail.OrderOrderDetailResponse;
//import com.doudian.open.api.order_orderDetail.param.OrderOrderDetailParam;
import com.doudian.open.api.product_listV2.ProductListV2Request;
import com.doudian.open.api.product_listV2.ProductListV2Response;
import com.doudian.open.api.product_listV2.param.ProductListV2Param;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 16:08
 * @Description: 示例 - 访问平台api获取订单详情
 */
@ExtensionService("orderDetailGet")
@Slf4j
public class OrderDetailGetDemoService implements ExtensionServiceHandler<OrderDetailGetRequest, OrderDetailGetResponse> {

    @Override
    public BaseResponse<OrderDetailGetResponse> handle(BaseRequest<OrderDetailGetRequest> req) {
        // 拿到前端请求的data数据
        OrderDetailGetRequest data = req.getData();

        ProductListV2Request request = new ProductListV2Request();
        ProductListV2Param param = request.getParam();
        param.setPage(1L);
        param.setSize(10L);


        System.out.println("getAuthId--" + req.getAuthId() + "---getAuthId");
        System.out.println("param--" + param + "---param");

        ProductListV2Response response = OpenApiUtil.invokeOpenApi(req.getAuthId(), request);

        System.out.println("response--" + response + "---response");

        OrderDetailGetResponse orderDetailGetResponse = new OrderDetailGetResponse();
        orderDetailGetResponse.setDetail(JSONObject.toJSONString(response.getData()));


        // 创建订单详情api请求对象，把前端数据赋值进去
//        OrderOrderDetailRequest request = new OrderOrderDetailRequest();
//        OrderOrderDetailParam param = request.getParam();
//        param.setShopOrderId(data.getTid());

        // 执行抖店开放平台api并获取结果
//        OrderOrderDetailResponse response = OpenApiUtil.invokeOpenApi(req.getAuthId(), request);

        // 创建返回给前端的响应对象，把抖店api响应结果赋值进去（这里偷了个懒，直接把结果用json字符串返回了，正式开发还是建议定义响应字段）
//        OrderDetailGetResponse orderDetailGetResponse = new OrderDetailGetResponse();
//        orderDetailGetResponse.setDetail(JSONObject.toJSONString(response.getData()));

        BaseResponse<OrderDetailGetResponse> rsp = BaseResponse.<OrderDetailGetResponse>builder()
                .success(true)
                .data(orderDetailGetResponse)
                .build();
        return rsp;
    }
}
