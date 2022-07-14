package com.tanhua.model.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    /**
     * 由于userinfo表和user表之间是一对一关系
     *   userInfo的id来源于user表的id
     */
    @TableId(type= IdType.INPUT)
    private Long id; //用户id
    @ExcelProperty(index = 0,value = "昵称")
    private String nickname; //昵称
    @ExcelProperty(index = 1,value = "用户头像")
    private String avatar; //用户头像
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(index = 2,value = "生日")
    private String birthday; //生日
    @ExcelProperty(index = 3,value = "性别")
    private String gender; //性别
    @ExcelProperty(index = 4,value = "年龄")
    private Integer age; //年龄
    @ExcelProperty(index = 5,value = "城市")
    private String city; //城市
    @ExcelProperty(index = 6,value = "收入")
    private String income; //收入
    @ExcelProperty(index = 7,value = "学历")
    private String education; //学历
    @ExcelProperty(index = 8,value = "行业")
    private String profession; //行业
    @ExcelProperty(index = 9,value = "婚姻状态")
    private Integer marriage; //婚姻状态
    @ExcelProperty(index = 10,value = "用户标签")
    private String tags; //用户标签：多个用逗号分隔
    @ExcelProperty(index = 11,value = "封面图片")
    private String coverPic; // 封面图片
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 12,value = "创建时间")
    private Date created;
    @ExcelProperty(index = 13,value = "修改时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updated;

    //用户状态,1为正常，2为冻结
    @TableField(exist = false)
    private String userStatus = "1";
}