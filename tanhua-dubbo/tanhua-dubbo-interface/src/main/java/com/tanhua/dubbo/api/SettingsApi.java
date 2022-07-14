package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Settings;

public interface SettingsApi {
    //根据id查询用户通用设置
    Settings findByUserId(Long userId);

    //保存用户通用设置
    void save(Settings settings);

    //更新用户通用设置
    void update(Settings settings);
}
