/**
 * Project Name:cloud-api-security
 * File Name:ProgressRankingVO.java
 * Package Name:com.fulan.api.security.vo
 * Date:2018年1月30日下午8:30:30
 * Copyright (c) 上海复深蓝软件股份有限公司.
 *
*/

package com.fulan.api.personnel.vo;

import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName:ProgressRankingVO
 * Reason:	 TODO ADD REASON
 * Date:     2018年1月30日 下午8:30:30 
 * @author   chen.zhuang
 * @version  
 * @since    JDK 1.8 
 */
@ApiModel(value = "ProgressRankingVO", description = "进度排名")
@Data
public class ProgressRankingVO {
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "当前用户数")
	private long currentNum;


	
	@ApiModelProperty(value = "总用户数")
	private String countNum;
}

