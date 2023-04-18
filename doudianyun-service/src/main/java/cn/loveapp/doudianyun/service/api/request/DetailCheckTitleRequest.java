package cn.loveapp.doudianyun.service.api.request;

import lombok.Data;

/**
 * 标题检测
 */
@Data
public class DetailCheckTitleRequest {
    private String title;
    private String productId;
    private String cid;
}
