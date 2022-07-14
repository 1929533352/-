package com.itheima.test;

import com.alibaba.excel.EasyExcel;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.server.AppServerApplication;
import com.tanhua.server.service.UserInfoService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
class WriteTest {
    @Autowired
    private static UserInfoService userInfoApi;
    public static void main(String[] args) {
        String fileName = "D:\\12.xlsx";

        //文件路径 或 输入输出流  实体类           模板名称
        EasyExcel.write(fileName,UserInfo.class).sheet("用户信息")
                //要写入的数据
                .doWrite(data());
    }

    //循环设置要添加的数据，最终封装到list集合中
    private static List<UserInfo> data() {
        List<UserInfo> list = (List<UserInfo>) userInfoApi.findById(1L);
        return list;
    }

    }

