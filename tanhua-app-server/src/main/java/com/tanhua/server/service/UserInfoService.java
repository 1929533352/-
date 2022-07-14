package com.tanhua.server.service;

import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OSSTemplate;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.ErrorResult;
import com.tanhua.model.vo.UserInfoVo;
import com.tanhua.server.exception.BusinessException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserInfoService {
    @Autowired
    private OSSTemplate ossTemplate;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;
    @DubboReference
    private UserInfoApi userInfoApi;

    /**
     * 保存用户
     *
     * @param userInfo
     */
    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }

    //更新用户头像
    public void updateHeda(MultipartFile headPhoto, Long id) throws IOException {
        //1.将图片上传到阿里云oss
        String imgUrl = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        //2.调用百度云判断是否包含人脸
        boolean detect = aipFaceTemplate.detect(imgUrl);
        //2.1flash 抛出异常
        if (!detect) {
            throw new BusinessException(ErrorResult.faceError());
        } else {
            //2.2 true 调用api更新
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setAvatar(imgUrl);
            userInfoApi.update(userInfo);
        }

    }

    //根据id进行查询
    public UserInfoVo findById(Long userId) {
        UserInfoVo userInfoVo = new UserInfoVo();

        UserInfo userInfo = userInfoApi.findById(userId);

        BeanUtils.copyProperties(userInfo, userInfoVo);//仅copy同名同类型的属性
        if (userInfo.getAge() != null) {
            userInfoVo.setAge(userInfo.getAge().toString());
        }
        return userInfoVo;
    }

    //更新用户资料
    public void UpdateById(UserInfo userInfo) {
        userInfoApi.update(userInfo);
    }

}
