package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.common.service.DetectionTitleService;
import cn.loveapp.doudianyun.common.service.DyApiService;
import cn.loveapp.doudianyun.common.service.DetailCheckTitleService;
import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;
import cn.loveapp.doudianyun.service.api.request.DetailCheckTitleRequest;
import cn.loveapp.doudianyun.service.api.request.GetAllItemListRequest;
import cn.loveapp.doudianyun.service.api.request.OpenApiProxyRequest;
import cn.loveapp.doudianyun.service.api.response.DetailCheckTitleResponse;
import cn.loveapp.doudianyun.service.service.ItemDetectionListService;
import com.alibaba.fastjson.JSONObject;
import com.doudian.open.api.product_listV2.ProductListV2Response;
import com.doudian.open.api.product_listV2.data.CategoryDetail;
import com.doudian.open.api.product_listV2.data.DataItem;
import com.doudian.open.api.product_qualityDetail.ProductQualityDetailResponse;
import com.doudian.open.api.product_qualityDetail.data.FieldProblemItem;
import com.doudian.open.api.product_qualityDetail.data.ProductQualityDetailData;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ExtensionService("allItemList.get")
@Slf4j
public class GetAllItemListService implements ExtensionServiceHandler<GetAllItemListRequest, List<DataItem>> {

    @Autowired
    private DyApiService dyApiService;

    @Autowired
    private DetectionTitleService detectionTitleService;

    @Autowired
    private DetailCheckTitleService detailCheckTitleService;

    @Autowired
    private ItemDetectionListService itemDetectionListService;

    @Override
    public BaseResponse<List<DataItem>> handle(BaseRequest<GetAllItemListRequest> req) {

        Long page = 1L;
        Long size = 100L;
        Long status = 0L;
        GetAllItemListRequest data = req.getData();
        if (data != null) {
            page = data.getPage();
            size = data.getSize();
            status = data.getStatus();
        }

        // 全部的列表数据
        List<DataItem> arrList = new ArrayList<>();

        ProductListV2Response itemList = dyApiService.getItemList(status, page, size);

        if (Objects.equals(itemList.getCode(), "10000")) {
            // 商品获取成功
            List<DataItem> curArrList = itemList.getData().getData();
            // 获取数据加入列表
            if (!curArrList.isEmpty()) {
                arrList.addAll(curArrList);
            }

            // 请求成功
            long total = itemList.getData().getTotal();
            long cycleNum = 3; // (int) Math.ceil((double) total / size); // 需要获取多少次才能拿到全部的数据
            long curNum = 2;
            int errNum = 0; // 记录失败次数

            while (curNum <= cycleNum) {
                if (errNum > 3) {
                    // 失败次数超过3次了，尝试获取下一页吧
                    errNum = 0;
                    curNum++;
                    continue;
                }
                ProductListV2Response singleItemList = dyApiService.getItemList(status, curNum, size);
                if (Objects.equals(singleItemList.getCode(), "10000")) {
                    // 商品获取成功
                    List<DataItem> newArrList = singleItemList.getData().getData();
                    // 获取数据加入列表
                    if (!newArrList.isEmpty()) {
                        arrList.addAll(newArrList);
                    }
                    // 清空失败次数
                    errNum = 0;
                    // 继续获取下一页商品
                    curNum++;
                } else {
                    // 失败次数+1
                    errNum++;
                }
            }
        }

        // 存储所有商品的类目信息
//        Map<String, Map<String, Object>> cidResultData = new HashMap<>();

        for (DataItem arrListItem: arrList) {
            // 类目信息
            Map<String, String> cidData = detectionTitleService.getCategoryDetail(arrListItem.getCategoryDetail());
            int cid = Integer.parseInt(cidData.get("cid"));

//            // 记录类目信息
//            Map<String, Object> curCidData = cidResultData.get(cid);
//            if (curCidData.get(cid) == null) {
//                Map<String, Object> newCidData = new HashMap<>();
//                newCidData.put("babyIndex", 1);
//                newCidData.put("cidName", cidData.get("cidName"));
//                newCidData.put("cidStr", cidData.get("cidStr"));
//                cidResultData.put(cid, newCidData);
//            } else {
//                Map<String, Object> oldCurCidData = cidResultData.get(cid);
//                int curIndex = (Integer) oldCurCidData.get("babyIndex");
//                oldCurCidData.put("babyIndex", ++curIndex);
//                cidResultData.put(cid, oldCurCidData);
//            }

            // 标题检测结果
            Map<String, Object> titleResult = detailCheckTitleService.getDetailCheckTitle(arrListItem.getName(), String.valueOf(arrListItem.getProductId()), cid);

            ItemDetectionList singleItemList = new ItemDetectionList();
            singleItemList.setNick("开放平台测试专用店");
            singleItemList.setUserId("123456789");
            singleItemList.setCid(cid);
            singleItemList.setStatus(status.intValue());
            singleItemList.setTitle(arrListItem.getName());
            singleItemList.setProductId(arrListItem.getProductId().toString());
            singleItemList.setImg(arrListItem.getImg());
            singleItemList.setProductType(arrListItem.getProductType().intValue());
            singleItemList.setCheckStatus(arrListItem.getCheckStatus().intValue());
            singleItemList.setTitleType((int) titleResult.get("titleType"));
            singleItemList.setDetectionResult(JSONObject.toJSONString(titleResult));
            singleItemList.setExternalData(JSONObject.toJSONString(titleResult.get("externalData")));
            // 获取当前时间
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String gmtCreate = now.format(formatter);

            // 获取当前时间
            Date gmtCreate = new Date();
            singleItemList.setGmtCreate(gmtCreate); // 针对库商品创建时间
            singleItemList.setGmtModified(gmtCreate); // 针对库修改时间
            Date updateTime = new Date(arrListItem.getUpdateTime() * 1000);
            singleItemList.setUpdateTime(updateTime); // 针对商品，官方返回的商品修改时间

            // 验证是否已经存在数据
            Boolean hasData = itemDetectionListService.getIsHasItem("123456789", String.valueOf(arrListItem.getProductId()));
            if (hasData) {
                // 更改数据
                itemDetectionListService.updateItemDetectionList(singleItemList);
            } else {
                // 数据入库
                itemDetectionListService.addItemDetectionList(singleItemList);
            }
        }

        BaseResponse<List<DataItem>> rsp = BaseResponse.<List<DataItem>>builder()
                .success(true)
                .code("10000")
                .message("数据获取成功")
                .data(arrList)
                .build();
        return rsp;
    }
}
