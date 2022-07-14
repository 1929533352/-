package com.tanhua.server.controller;

import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.SettingsVo;
import com.tanhua.server.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;


    /**
     * 查询用户通用设置
     *
     * @return
     */
    @GetMapping("/settings")
    public ResponseEntity settings() {
        SettingsVo vo = settingsService.settings();
        return ResponseEntity.ok(vo);
    }

    /**
     * 保存用户陌生人问题
     *
     * @param map
     * @return
     */
    @PostMapping("/questions")
    public ResponseEntity blacklist(@RequestBody Map map) {
        String content = (String) map.get("content");
        settingsService.questions(content);
        return ResponseEntity.ok(null);
    }

    /**
     * 保存用户通用设置
     *
     * @param map
     * @return
     */
    @PostMapping("/notifications/setting")
    public ResponseEntity notifications(@RequestBody Map map) {
        settingsService.notifications(map);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询黑名单列表
     */
    @GetMapping("/blacklist")
    public ResponseEntity blacklist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        //1、调用service查询
        PageResult pr = settingsService.blacklist(page, size);
        //2、构造返回
        return ResponseEntity.ok(pr);
    }

    /**
     * 取消黑名单
     */
    @DeleteMapping("/blacklist/{uid}")
    public ResponseEntity blacklist(@PathVariable("uid") Long blackUserId) {

        settingsService.deleteByID(blackUserId);
        return ResponseEntity.ok(null);
    }
}

