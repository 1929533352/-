package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.RecommendUser;
import com.tanhua.model.vo.PageResult;

public interface RecommendUserApi {
    //今日佳人
    RecommendUser queryWithMaxScore(Long toUserId);

    PageResult queryRecommendUserList(Integer page, Integer pagesize, Long userId);
}