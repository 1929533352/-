package com.tanhua.server.controller;

import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.UserInfoVo;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.service.UserInfoService;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    /**
     * 查询用户信息
     *
     * @param token
     * @param userID
     * @return
     */
    @GetMapping
    public ResponseEntity findById(@RequestHeader("Authorization") String token, Long userID) {


        if (userID == null) {
            userID = UserHolder.getUserId();
        }
        //调用service进行查询
        UserInfoVo userInfo = userInfoService.findById(userID);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param token
     * @return
     */
    @PutMapping
    public ResponseEntity update(@RequestBody UserInfo userInfo, @RequestHeader("Authorization") String token) {
        //设置用户id
        userInfo.setId(UserHolder.getUserId());
        //调用service进行查询
        userInfoService.UpdateById(userInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 修改用户头像
     *
     * @param token
     * @return
     */
    @PostMapping("/header")
    public ResponseEntity updateHeader(MultipartFile headPhoto,
                                       @RequestHeader("Authorization") String token) throws IOException {
        //调用方法更新用户头像
        userInfoService.updateHeda(headPhoto, UserHolder.getUserId());
        return ResponseEntity.ok(null);
    }


    /**
     * 修改手机号,发送验证码
     */
    @PostMapping("/phone/sendVerificationCode")
    public ResponseEntity updatePhone() {
        //1.获取当前用户手机号
        String mobile = UserHolder.getMobile();
        userService.sendMsg(mobile);
        return ResponseEntity.ok(null);
    }

    /**
     * 修改手机号,校验验证码
     */
    @PostMapping("/phone/checkVerificationCode")
    public ResponseEntity verificationCode(@RequestBody Map map) {
        //1.获取验证码
        String code = (String) map.get("verificationCode");
        //1.获取当前用户手机号
        String mobile = UserHolder.getMobile();
        Map verification = userService.verificationCode(code);

        return ResponseEntity.ok(verification);
    }

    /**
     * 修改手机号,保存新手机号
     */
    @PostMapping("/phone")
    public ResponseEntity save(@RequestBody Map map) {
        //1.获取新的手机号
        String phone = (String) map.get("phone");
        userService.updatePhone(phone);
        return ResponseEntity.ok(null);
    }

}
