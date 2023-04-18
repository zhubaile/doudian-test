package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.service.DetailCheckTitleService;
import cn.loveapp.doudianyun.common.service.DetectionTitleService;
import cn.loveapp.doudianyun.common.service.DyApiService;
import cn.loveapp.doudianyun.common.service.RedisService;
import cn.loveapp.doudianyun.db.common.entity.DditoOrderSearchItem;
import cn.loveapp.doudianyun.service.api.domain.DditoOrderSearchItemDTO;
import cn.loveapp.doudianyun.service.api.request.DetailCheckTitleRequest;
import cn.loveapp.doudianyun.service.api.response.DetailCheckTitleResponse;
import com.doudian.open.api.compass_CommonProductQueryData.CompassCommonProductQueryDataResponse;
import com.doudian.open.api.compass_getProductSaleData.CompassGetProductSaleDataResponse;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailResponse;
import com.doudian.open.api.product_qualityDetail.data.FieldProblemItem;
import com.doudian.open.api.product_qualityDetail.data.ProductQualityDetailData;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 标题检测
 */
@ExtensionService("detailCheckTitle.get")
@Slf4j
public class DetailCheckTitleGetService implements ExtensionServiceHandler<DetailCheckTitleRequest, DetailCheckTitleResponse> {

    @Autowired
    private DetectionTitleService detectionTitleService;

    @Autowired
    private DyApiService dyApiService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private DetailCheckTitleService detailCheckTitleService;

    @Override
    public BaseResponse<DetailCheckTitleResponse> handle(BaseRequest<DetailCheckTitleRequest> req) {
        try {

            String title = "";
            String productId = "";
            String cid = "";
            DetailCheckTitleRequest data = req.getData();
            if (data != null) {
                title = data.getTitle();
                productId = data.getProductId();
                cid = data.getCid();
            }

            // 返回内容
            DetailCheckTitleResponse detailCheckTitleData = new DetailCheckTitleResponse();

            if (title.isEmpty() || productId.isEmpty() || cid.isEmpty()) {
                BaseResponse<DetailCheckTitleResponse> rsp = BaseResponse.<DetailCheckTitleResponse>builder()
                        .success(true)
                        .code("40004")
                        .message("缺少必填参数")
                        .data(detailCheckTitleData)
                        .build();
                return rsp;
            }

            Map<String, Object> titleResult = detailCheckTitleService.getDetailCheckTitle(title, productId, Integer.parseInt(cid));

            detailCheckTitleData.setTitleResult(titleResult);
            BaseResponse<DetailCheckTitleResponse> rsp = BaseResponse.<DetailCheckTitleResponse>builder()
                    .success(true)
                    .code("40004")
                    .message("缺少参数，没有获取到标题")
                    .data(detailCheckTitleData)
                    .build();
            return rsp;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
