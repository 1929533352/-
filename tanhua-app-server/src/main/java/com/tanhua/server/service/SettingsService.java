package com.tanhua.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.dubbo.api.BlackListApi;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.SettingsApi;
import com.tanhua.model.domain.Question;
import com.tanhua.model.domain.Settings;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.SettingsVo;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SettingsService {

    @DubboReference
    private QuestionApi questionApi;
    @DubboReference
    private SettingsApi settingsApi;
    @DubboReference
    private BlackListApi blackListApi;

    //查询用户通用设置
    public SettingsVo settings() {
        //1.获取用户id
        SettingsVo vo = new SettingsVo();
        Long userId = UserHolder.getUserId();
        vo.setId(userId);
        //2.获取用户的手机号码
        String mobile = UserHolder.getMobile();
        vo.setPhone(mobile);
        //3.获取用户的陌生人问题
        Question question = questionApi.findById(userId);
        String txt = question == null ? "你喜欢java吗" : question.getTxt();
        //4.获取用户的APP通知开关数据
        Settings settings = settingsApi.findByUserId(userId);
        if (settings != null) {
            vo.setGonggaoNotification(settings.getGonggaoNotification());
            vo.setPinglunNotification(settings.getPinglunNotification());
            vo.setLikeNotification(settings.getLikeNotification());
        }
        return vo;
    }

    //保存用户陌生人问题
    public void questions(String content) {
        //1.获取用户id
        Long userId = UserHolder.getUserId();
        //2.判断陌生人问题是否为空
        Question question = questionApi.findById(userId);
        if (question == null) {
            //3.为空则保存
            question = new Question();
            question.setTxt(content);
            question.setUserId(userId);
            questionApi.save(question);
        } else {
            //4.不为空则更新
            question.setTxt(content);
            questionApi.update(question);
        }

    }

    //保存用户通用设置
    public void notifications(Map map) {
        //获取map集合中的参数
        Boolean likeNotification = (Boolean) map.get("likeNotification");
        Boolean pinglunNotification = (Boolean) map.get("pinglunNotification");
        Boolean gonggaoNotification = (Boolean) map.get("gonggaoNotification");
        //1.获取用户id
        Long userId = UserHolder.getUserId();
        //2.查询用户通用设置是否为空
        Settings settings = settingsApi.findByUserId(userId);
        //3.为空则保存
        if (settings == null) {
            settings = new Settings();
            settings.setUserId(userId);
            settings.setGonggaoNotification(gonggaoNotification);
            settings.setPinglunNotification(pinglunNotification);
            settings.setLikeNotification(likeNotification);
            settingsApi.save(settings);
        } else {
            settings.setGonggaoNotification(gonggaoNotification);
            settings.setPinglunNotification(pinglunNotification);
            settings.setLikeNotification(likeNotification);
            settingsApi.update(settings);
        }
        //4.不为空则更新
    }

    //查询用户黑名单
    public PageResult blacklist(int page, int size) {
        //1、获取当前用户的id
        Long userId = UserHolder.getUserId();
        //2、调用API查询用户的黑名单分页列表  Ipage对象
        IPage<UserInfo> iPage = blackListApi.findByUserId(userId, page, size);
        //3、对象转化，将查询的Ipage对象的内容封装到PageResult中
        PageResult pr = new PageResult(page, size, iPage.getTotal(), iPage.getRecords());
        //4、返回
        return pr;
    }

    //根据id取消黑名单
    public void deleteByID(Long blackUserId) {
        //1、获取当前用户id
        Long userId = UserHolder.getUserId();
        //2、调用api删除
        blackListApi.deleteById(userId,blackUserId);
    }


}
