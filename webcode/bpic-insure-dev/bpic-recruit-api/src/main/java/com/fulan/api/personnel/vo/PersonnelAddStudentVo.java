package com.fulan.api.personnel.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 新增学员
 *
 * @author threedong
 */
@Data
public class PersonnelAddStudentVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "id")
    private Long id;

    @ApiModelProperty(value = "DMS返回的agentcode", name = "agentCode")
    private String agentCode;


    @ApiModelProperty(value = "姓名", name = "name")
    private String name;

    @ApiModelProperty(value = "核定职级", name = "confirmPosition")
    private String confirmPosition;

    @ApiModelProperty(value = "拟定职级", name = "confirmPosition")
    private String protocolPosition;

    @ApiModelProperty(value = "人才所在分公司id", name = "companyId")
    private String companyId;

    @ApiModelProperty(value = "创建该增员人员分公司id", name = "orgId")
    private String orgId;

    @ApiModelProperty(value = "电话", name = "telephone")
    private String telephone;

    @ApiModelProperty(value = "手机号", name = "cellphone")
    private String cellphone;

    @ApiModelProperty(value = "电子邮箱", name = "email")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "创建者", name = "createUser")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty(value = "来源渠道", name = "channel")
    private String channel;


    /**
     * 方便后台从jsp接收处理
     *
     * @return tostring
     */
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String createTimes = sdf.format(createTime);
        return "{'id':'" + id + "',"
                + "'agentCode':'" + agentCode + "',"
                + "'name':'" + name + "',"
                + "'confirmPosition':'" + confirmPosition + "',"
                + "'companyId':'" + companyId + "',"
                + "'orgId':'" + orgId + "',"
                + "'telephone':'" + telephone + "',"
                + "'cellphone':'" + cellphone + "',"
                + "'email':'" + email + "',"
                + "'createTime':'" + createTimes + "',"
                + "'createUser':'" + createUser + "',"
                + "'channel':'" + channel + "'"
                + "}";
    }


}
