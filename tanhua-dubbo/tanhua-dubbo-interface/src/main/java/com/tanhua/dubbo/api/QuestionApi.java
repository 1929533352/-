package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Question;

public interface QuestionApi {
    //根据id查询用户陌生人问题
    Question findById(Long userId);
    //保存陌生人问题
    void save(Question question);
    //更新陌生人问题
    void update(Question question);
}
