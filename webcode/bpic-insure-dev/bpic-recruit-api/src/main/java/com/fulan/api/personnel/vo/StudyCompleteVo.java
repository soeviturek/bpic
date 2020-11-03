package com.fulan.api.personnel.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudyCompleteVo {

    private String phone;//人员手机号

    private String name;//人员名称

    private String personnelId;

    private String channel;//渠道

    private String checkResult;//是否通过   1:通过，0不通过

    private String id;//当前人的ID

}
