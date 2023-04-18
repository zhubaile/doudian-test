package cn.loveapp.doudianyun.service.api.response;

import cn.loveapp.doudianyun.service.api.domain.DditoOrderSearchItemDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 10:58
 * @Description:
 */
@Data
public class DditoOrderSearchItemGetResponse {

    private List<DditoOrderSearchItemDTO> dditoOrderSearchItemDTOList;
}
