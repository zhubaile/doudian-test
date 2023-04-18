package cn.loveapp.doudianyun.service.service.impl;

import cn.loveapp.doudianyun.db.common.entity.DditoOrderSearchItem;
import cn.loveapp.doudianyun.db.common.entity.PromotionItem;
import cn.loveapp.doudianyun.db.common.mapper.DditoOrderSearchItemMapper;
import cn.loveapp.doudianyun.db.common.mapper.PromotionMapper;
import cn.loveapp.doudianyun.service.api.domain.DditoOrderSearchItemDTO;
import cn.loveapp.doudianyun.service.api.domain.PromotionItemDTO;
import cn.loveapp.doudianyun.service.api.request.DditoOrderSearchItemGetRequest;
import cn.loveapp.doudianyun.service.api.request.PromotionItemRequest;
import cn.loveapp.doudianyun.service.api.response.DditoOrderSearchItemGetResponse;
import cn.loveapp.doudianyun.service.api.response.PromotionItemResponse;
import cn.loveapp.doudianyun.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/11 11:15
 * @Description:
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired
    private DditoOrderSearchItemMapper dditoOrderSearchItemMapper;

    @Override
    public DditoOrderSearchItemGetResponse dditoOrderSearchItemGet(DditoOrderSearchItemGetRequest request) {
        DditoOrderSearchItemGetResponse response = new DditoOrderSearchItemGetResponse();
        List<DditoOrderSearchItem> dditoOrderSearchItems = dditoOrderSearchItemMapper.queryByNick(request.getNick());
        List<DditoOrderSearchItemDTO> dditoOrderSearchItemDTOList = new ArrayList<>();
        for (DditoOrderSearchItem dditoOrderSearchItem : dditoOrderSearchItems) {
            DditoOrderSearchItemDTO dditoOrderSearchItemDTO = new DditoOrderSearchItemDTO();
            BeanUtils.copyProperties(dditoOrderSearchItem, dditoOrderSearchItemDTO);
            dditoOrderSearchItemDTOList.add(dditoOrderSearchItemDTO);
        }
        response.setDditoOrderSearchItemDTOList(dditoOrderSearchItemDTOList);
        return response;
    }

    @Autowired
    private PromotionMapper promotionMapper;

    public String promotionItemInter (){ // PromotionItemResponse PromotionItemRequest request
        PromotionItemResponse response = new PromotionItemResponse();

        String asd = promotionMapper.interPromotion("");
        System.out.println("asd--" + asd + "---asd");
//        List<DditoOrderSearchItem> dditoOrderSearchItems = dditoOrderSearchItemMapper.queryByNick(request.getNick());
//        List<PromotionItemDTO> PromotionItemDTOList = new ArrayList<>();
//        for (DditoOrderSearchItem dditoOrderSearchItem : dditoOrderSearchItems) {
//            DditoOrderSearchItemDTO dditoOrderSearchItemDTO = new DditoOrderSearchItemDTO();
//            BeanUtils.copyProperties(dditoOrderSearchItem, dditoOrderSearchItemDTO);
//            dditoOrderSearchItemDTOList.add(dditoOrderSearchItemDTO);
//        }
//        response.setPromotionItemDTOList(PromotionItemDTOList);
        return "";
    }
}
