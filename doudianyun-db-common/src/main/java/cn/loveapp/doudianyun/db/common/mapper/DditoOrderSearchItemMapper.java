package cn.loveapp.doudianyun.db.common.mapper;

import cn.loveapp.doudianyun.db.common.entity.DditoOrderSearchItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zhongzijie
 * @Date: 2023/4/10 22:17
 * @Description:
 */
@Mapper
public interface DditoOrderSearchItemMapper {

    /**
     * 通过nick查询订购记录
     *
     * @param nick
     * @return
     */
    List<DditoOrderSearchItem> queryByNick(@Param("nick") String nick);
}
