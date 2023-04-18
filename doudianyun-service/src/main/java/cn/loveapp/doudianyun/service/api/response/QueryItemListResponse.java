package cn.loveapp.doudianyun.service.api.response;

import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;
import lombok.Data;

import java.util.List;

@Data
public class QueryItemListResponse {
    private int total;
    private List<ItemDetectionList> dataList;
}
