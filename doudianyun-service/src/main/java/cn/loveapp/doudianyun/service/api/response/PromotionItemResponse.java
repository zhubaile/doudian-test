package cn.loveapp.doudianyun.service.api.response;

import cn.loveapp.doudianyun.service.api.domain.PromotionItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class PromotionItemResponse {
    private List<PromotionItemDTO> PromotionItemDTOList;
}
