package com.tanhua.dubbo.api;

import com.tanhua.model.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoApi {
    //保存用户信息
    void save(UserInfo userInfo);

    //更新用户信息
    void update(UserInfo userInfo);
    //根据id进行查询
    UserInfo findById(Long userID);


    Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo userInfo);

}
