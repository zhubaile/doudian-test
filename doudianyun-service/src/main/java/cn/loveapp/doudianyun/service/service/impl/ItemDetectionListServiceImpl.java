package cn.loveapp.doudianyun.service.service.impl;

import cn.loveapp.doudianyun.db.common.entity.ItemDetectionList;
import cn.loveapp.doudianyun.db.common.mapper.ItemDetectionListMapper;
import cn.loveapp.doudianyun.service.service.ItemDetectionListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ItemDetectionListServiceImpl implements ItemDetectionListService {

    @Autowired
    private ItemDetectionListMapper itemDetectionListMapper;

    @Override
    public Void addItemDetectionList(ItemDetectionList request) {
        itemDetectionListMapper.interDetectionList(request);
        return null;
    }

    @Override
    public Void updateItemDetectionList(ItemDetectionList request) {
        itemDetectionListMapper.updateDetectionList(request);
        return null;
    }

    @Override
    public List<ItemDetectionList> getItemDetectionList(ItemDetectionList request, int begin, int size) {
        List<ItemDetectionList> allData = itemDetectionListMapper.getDetectionList(request, begin, size);
        return allData;
    }

    @Override
    public int getItemDetectionListTotal(ItemDetectionList request) {
        int total = itemDetectionListMapper.getDetectionListTotal(request);
        return total;
    }


    @Override
    public Boolean getIsHasItem(String userId, String productId) {
        Boolean allData = itemDetectionListMapper.getIsHasItem(userId, productId);
        return allData;
    }
}
