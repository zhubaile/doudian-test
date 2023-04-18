package cn.loveapp.doudianyun.service.extensionservice;

import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;
import cn.loveapp.doudianyun.service.api.request.QueryItemListRequest;
import cn.loveapp.doudianyun.service.api.response.QueryItemListResponse;
import cn.loveapp.doudianyun.service.service.ItemDetectionListService;
import com.jinritemai.cloud.base.api.BaseRequest;
import com.jinritemai.cloud.base.api.BaseResponse;
import com.jinritemai.cloud.base.api.ExtensionService;
import com.jinritemai.cloud.base.api.ExtensionServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ExtensionService("queryItemList.get")
@Slf4j
public class QueryItemListService implements ExtensionServiceHandler<QueryItemListRequest, QueryItemListResponse> {

    @Autowired
    private ItemDetectionListService itemDetectionListService;

    @Override
    public BaseResponse<QueryItemListResponse> handle(BaseRequest<QueryItemListRequest> req) {
//        Integer page = 1;
//        int size = 100;
//        int status = 0;
        QueryItemListRequest data = req.getData();
        // 数据返回
        QueryItemListResponse listResult = new QueryItemListResponse();
        if (data == null) {
            // 数据返回
            BaseResponse<QueryItemListResponse> rsp = BaseResponse.<QueryItemListResponse>builder()
                    .success(true)
                    .code("40004")
                    .message("缺少参数")
                    .data(listResult)
                    .build();
            return rsp;
        }

        int page = data.getPage() == null ? 1 : data.getPage();
        int size = data.getSize() == null ? 10 : data.getSize();

        int begin = (page - 1) * size;

        ItemDetectionList getItemListParams = new ItemDetectionList();
        getItemListParams.setNick("开放平台测试专用店");
        getItemListParams.setUserId("123456789");
        // 在线--下线
        if (data.getStatus() != null) {
            getItemListParams.setStatus(data.getStatus());
        }
        // 优良差
        if (data.getTitleType() != null) {
            getItemListParams.setTitleType(data.getTitleType());
        }
        // 审核状态
        if (data.getCheckStatus() != null) {
            getItemListParams.setCheckStatus(data.getCheckStatus());
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
        String gmtCreate = now.format(formatter);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date gmtCreateDate;
        try {
            gmtCreateDate = format.parse(gmtCreate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // 插入今天的开始时间 2023-04-18 00:00:00
        getItemListParams.setGmtModified(gmtCreateDate);

//        Map<String, Object> searchParams1 = new HashMap<>();
//        searchParams1.put("nick", "开放平台测试专用店");
//        searchParams1.put("productId", "3611470921036124389");
//        // 数据查询1
//        List<ItemDetectionList> itemListData = itemDetectionListService.getIsHasItem(searchParams1);

        // 数据查询
        List<ItemDetectionList> itemListData = itemDetectionListService.getItemDetectionList(getItemListParams, begin, size);
        // 获取总数
        int totalData = itemDetectionListService.getItemDetectionListTotal(getItemListParams);
        // 数据返回
        listResult.setTotal(totalData);
        listResult.setDataList(itemListData);
        BaseResponse<QueryItemListResponse> rsp = BaseResponse.<QueryItemListResponse>builder()
                .success(true)
                .code("10000")
                .message("数据获取成功")
                .data(listResult)
                .build();
        return rsp;
    }
}
