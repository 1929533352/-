package com.tanhua.server.service;

import com.tanhua.autoconfig.template.SmsTemplate;
import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import com.tanhua.model.vo.ErrorResult;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private SmsTemplate template;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @DubboReference
    private UserApi userApi;


    /**
     * 发送短信验证码
     *
     * @param phone
     */
    public void sendMsg(String phone) {
        //1、随机生成6位数字
        //String code = RandomStringUtils.randomNumeric(6);
        String code = "888888";
        //2、调用template对象，发送手机短信
        //template.sendSms(phone,code);
        //3、将验证码存入到redis
        //redisTemplate.opsForValue().set("CHECK_CODE_" + phone, code, Duration.ofMinutes(5));
        redisTemplate.opsForValue().set("CHECK_CODE_" + phone, code, Duration.ofMinutes(5));

    }

    /**
     * 用户登录
     *
     * @param phone
     * @param code
     * @return
     */
    public Map loginVerification(String phone, String code) {
        //1、从redis中获取下发的验证码
        String redisCode = redisTemplate.opsForValue().get("CHECK_CODE_" + phone);
        //2、对验证码进行校验（验证码是否存在，是否和输入的验证码一致）
        if (StringUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            //验证码无效
            throw new BusinessException(ErrorResult.loginError());
        }
        //3、删除redis中的验证码
        redisTemplate.delete("CHECK_CODE_" + phone);
        //4、通过手机号码查询用户
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        //5、如果用户不存在，创建用户保存到数据库中
        if (user == null) {
       user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;
        }
        //6、通过JWTUtils生成token(存入id和手机号码)
        Map tokenMap = new HashMap();
        tokenMap.put("id", user.getId());
        tokenMap.put("mobile", phone);
        String token = JwtUtils.getToken(tokenMap);
        //7、构造返回值
        Map retMap = new HashMap();
        retMap.put("token", token);
        retMap.put("isNew", isNew);

        return retMap;
    }
    //修改手机号,校验验证码是否正确
    public Map verificationCode(String code) {
        //1.获取用户手机号
        String mobile = UserHolder.getMobile();
        //2.获取下发到redis的验证码
        String redisCode = redisTemplate.opsForValue().get("CHECK_CODE_" + mobile);
        boolean verification;
        //3.校验验证码是否一致
        if (StringUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            verification =false;
        }else{
            verification = true;
        }
        redisTemplate.delete("CHECK_CODE_" + mobile);
        Map retMap = new HashMap();
        retMap.put("verification",verification);
        return retMap;
    }
    //修改手机号,保存新的手机号
    public void updatePhone(String phone) {
        //1.获取当前用户旧手机号
        String mobile = UserHolder.getMobile();
        //2.调用api进行查询
        User user = userApi.findByMobile(mobile);
        user.setMobile(phone);
        userApi.update(user);

    }
}
