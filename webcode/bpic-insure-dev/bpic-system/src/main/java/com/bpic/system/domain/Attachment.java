package com.bpic.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="附件对象",description="附件对象")
public class Attachment {
	 /**
	     * 主键id
	     */
    @ApiModelProperty(value="主键",name="id",example="1")
    @JsonSerialize(using = LongJsonSerializer.class)
   	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;
    @ApiModelProperty(value="关联表名称",name="tableName",example="1")
    @TableField("table_name")
    private String tableName;
    @ApiModelProperty(value="关联编号",name="hostId",example="1")
    @TableField("host_id")
    private String hostId;
    @ApiModelProperty(value="附件名称-系统命名",name="name",example="1")
    private String name;
    @ApiModelProperty(value="附件大小",name="size",example="1")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long size;
   
    private String path;
   
    private String description;
    @TableField("file_ext")
    private String fileExt;
    @TableField("original_name")
    private String originalName;
   
    private Integer seq;
   
    private Integer type;

    @ApiModelProperty(value="附件类型",name="category",example="1")
    private String category;

    private String url;

    private Integer state;
    @ApiModelProperty(value="是否删除",name="isDelete",example="1")
    @TableField("is_delete")
    private Integer isDelete;
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    @TableField("create_user")
    private Long createUser;
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

   
    
}