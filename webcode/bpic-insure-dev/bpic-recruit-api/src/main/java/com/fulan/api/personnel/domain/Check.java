package com.fulan.api.personnel.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 陈磊
 * @date: 2019/9/20
 * Time:           11:47
 * Version:        1.0.0
 * Description:    Check类用于——资料核查
 */
@Data
@TableName("er_check")
public class Check {

    private Long id;

    private String personnelId;

//    1:通过  2:不通过
    private int checkResult;

//    审核意见
    private String checkOpinion;

//    审核结果_白底免冠证件照
    private int bareheadedPhoto;

//    审核结果_身份证件国辉面
    private int idCardFirst;

//    审核结果_身份证件人像面
    private int idCardSecond;

//    审核结果_学历证明
    private int educationalFirst;

//    审核结果_学信网验证
    private int educationalSecond;

//    审核结果_银行卡验证
    private int bankCardFirst;

//    审核结果_荣誉照片验证
    private int mdrt;

//    所有图片的附件id（,分割）
    private String imgIds;

    private Date createTime;

    private String createUser;

}
