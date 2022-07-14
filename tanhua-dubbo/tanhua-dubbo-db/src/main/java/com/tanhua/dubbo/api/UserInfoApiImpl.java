package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mappers.UserInfoMapper;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@DubboService
public class UserInfoApiImpl implements UserInfoApi {

    @Autowired
    private UserInfoMapper userInfoMapper;

    //保存用户信息
    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    //更新用户信息
    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    //查询用户信息
    @Override
    public UserInfo findById(Long userID) {
         return userInfoMapper.selectById(userID);
    }

    @Override
    public Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo userInfo) {
        QueryWrapper qw = new QueryWrapper();
        //1、用户id列表
        qw.in("id",ids);
        //2、添加筛选条件
        if(userInfo != null) {
            if(userInfo.getAge() != null) {
                qw.lt("age",userInfo.getAge());
            }
            if(!StringUtils.isEmpty(userInfo.getGender())) {
                qw.eq("gender",userInfo.getGender());
            }
        }
        List<UserInfo> list = userInfoMapper.selectList(qw);
        Map<Long, UserInfo> map = CollUtil.fieldValueMap(list, "id");
        return map;
    }


}
