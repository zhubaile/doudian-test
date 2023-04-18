package cn.loveapp.doudianyun.service.api.request;

import lombok.Data;

/**
 * 标题优化
 */
@Data
public class RecommendTitleRequest {
    private String title;
    private String productId;
    private String cid;
    private int optimizeNum; // 1=首次优化 2=重新优化
}
